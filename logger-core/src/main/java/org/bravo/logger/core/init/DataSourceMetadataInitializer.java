/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.init;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.bravo.logger.commons.model.TableMetadata;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hejianbing
 * @version @Id: DataSourceMetadataInitializer.java, v 0.1 2021年11月10日 00:01 hejianbing Exp $
 */
@Component
public class DataSourceMetadataInitializer implements InitializingBean {

    @Autowired(required = false)
    private DataSource                      dataSource;

    private String schema;

    private final Map<String, TableMetadata> tableNameMeta = new CaseInsensitiveMap<>();

    private AtomicBoolean isInit =  new AtomicBoolean(false);


    @Override
    public void afterPropertiesSet() throws Exception {
        if (dataSource == null || isInit.get()) {
            return;
        }

        Connection connection = this.dataSource.getConnection();
        try {

            this.schema = connection.getCatalog();

            ResultSet resultSet = connection.getMetaData().getTables(schema,
                connection.getMetaData().getUserName(), "%", new String[] { "TABLE" });
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                addMetadata(connection, tableName);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isInit.compareAndSet(false,true);
    }

    private void addMetadata(Connection connection, String tableName) {
        if (tableNameMeta.containsKey(tableName)) {
            return;
        }
        TableMetadata tableMetadata = new TableMetadata();
        tableMetadata.setSchema(schema);
        tableMetadata.setTableName(tableName);
        tableMetadata.setPrimaryKey(getPrimaryKey(connection, tableName));
        tableMetadata.setColumns(getTableColumns(connection, tableName));

        tableNameMeta.put(tableName, tableMetadata);
    }

    private String getPrimaryKey(Connection connection, String table) {
        String primaryKey = null;
        try {
            ResultSet resultSet = connection.getMetaData().getPrimaryKeys(null,
                connection.getMetaData().getUserName(), table);
            if (resultSet.next())
                primaryKey = resultSet.getString("COLUMN_NAME");
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return primaryKey;
    }

    private List<String> getTableColumns(Connection connection, String table) {
        List<String> columns = null;
        try {
            columns = new ArrayList<>();
            ResultSet resultSet = connection.getMetaData().getColumns(null,
                connection.getMetaData().getUserName(), table, "%");
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                columns.add(columnName);
            }
            resultSet.close();

            return columns;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columns;
    }

    public Map<String, TableMetadata> getTableMeta(){
        return Collections.unmodifiableMap(tableNameMeta);
    }

}