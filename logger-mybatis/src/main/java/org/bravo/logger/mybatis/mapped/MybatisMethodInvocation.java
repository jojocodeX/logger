/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.mybatis.mapped;

import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.bravo.logger.commons.enums.SqlCmd;
import org.bravo.logger.mybatis.executor.MybatisExecutor;
import org.bravo.logger.mybatis.mapped.parameter.ParameterMappingParser;
import org.bravo.logger.mybatis.mapped.sqlparser.DefaultSqlParser;
import org.bravo.logger.mybatis.mapped.sqlparser.SqlMeta;
import org.bravo.logger.mybatis.mapped.sqlparser.SqlParser;
import org.bravo.logger.mybatis.model.RowChangeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hejianbing
 * @version @Id: MybatisMethodInvocation.java, v 0.1 2021年11月11日 22:51 hejianbing Exp $
 */
@Getter
@Setter
public class MybatisMethodInvocation {

    private final MybatisExecutor        executor;
    private final MappedStatement        mappedStatement;
    private final Object                 parameterObject;
    private final SqlCmd                 sqlCmd;
    private final List<SqlMeta>          sqlMetas;
    private final BoundSql               boundSql;
    private final boolean                mapUnderscoreToCamelCase;
    private final SqlParser              sqlParser = new DefaultSqlParser();
    private final ParameterMappingParser parameterMappingParser;
    private List<RowChangeData>          rowChangeData;


    public MybatisMethodInvocation(final Executor executor,
                                   final MappedStatement mappedStatement,
                                   final Object parameter) throws Exception {
        this.mapUnderscoreToCamelCase = mappedStatement.getConfiguration().isMapUnderscoreToCamelCase();

        this.sqlCmd = SqlCmd.ofKey(mappedStatement.getSqlCommandType().name());
        this.executor = new MybatisExecutor(executor,mapUnderscoreToCamelCase);
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameter;
        this.boundSql = mappedStatement.getBoundSql(parameter);

        this.sqlMetas = sqlParser.getSqlMeta(mappedStatement, parameter);
        this.parameterMappingParser = new ParameterMappingParser(mappedStatement, boundSql, parameterObject);
    }

    public List<Map<String,Object>> getRequestParameters(){
        return parameterMappingParser.parse();
    }

    public Map<String, String> getColumnNameMapping() {
        String originalSql = boundSql.getSql();
        List<ParameterMapping> paramMappings = boundSql.getParameterMappings();
        if (paramMappings == null || paramMappings.size() <= 0) {
            return null;
        }
        Pattern p = Pattern.compile("(?is)([\\w]+)\\s*=\\s*\\?");
        String sqlList[] = originalSql.split(";");
        List<String> columnNames = new ArrayList<>(10);
        for (String sql : sqlList) {
            if (StringUtils.isBlank(sql)) {
                continue;
            }
            Matcher m = p.matcher(sql);
            while (m.find()) {
                String columnName = m.group(1);
                columnNames.add(columnName);
            }
        }
        Map<String, String> mapping = new CaseInsensitiveMap<>(columnNames.size());

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            String propertyName = paramMappings.get(i).getProperty();
            int dotIdx = propertyName.indexOf(".");
            String realName = dotIdx > 0 ? propertyName.substring(dotIdx + 1) : propertyName;

            String filed = mapUnderscoreToCamelCase
                ? CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName)
                : columnName;

            mapping.put(filed, realName);
        }
        return mapping;
    }

    public SelectMappedStatement getSelectMappedStatement(SqlMeta sqlMeta) {
        MappedStatementBuilder mappedStatementBuilder = new MappedStatementBuilder(this, sqlMeta);
        return mappedStatementBuilder.build();
    }

    public SqlMeta getSqlMeta(){
        return sqlMetas.get(0);
    }

}