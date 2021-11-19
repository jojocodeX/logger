/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.mybatis.mapped;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.bravo.logger.commons.context.LoggerContext;
import org.bravo.logger.commons.context.RequestScopeContext;
import org.bravo.logger.commons.enums.SqlCmd;
import org.bravo.logger.mybatis.mapped.sqlparser.SqlMeta;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hejianbing
 * @version @Id: MappedStatementBuilder.java, v 0.1 2021年11月12日 08:53 hejianbing Exp $
 */
public class MappedStatementBuilder {

    private final MybatisMethodInvocation invocation;

    private final SqlMeta          sqlMeta;

    public static final String CUSTOMQUERY = "_CUSTOMQUERY";

    public MappedStatementBuilder(MybatisMethodInvocation invocation, SqlMeta sqlMeta) {
        this.invocation = invocation;
        this.sqlMeta = sqlMeta;
    }

    public SelectMappedStatement build(){
        SelectMappedStatement result = new SelectMappedStatement();

        Table table = sqlMeta.getTable();
        MappedStatement mappedStatement = invocation.getMappedStatement();
        //构造select语句
        List<Column> columns = new ArrayList<>();
        //如果是update 精确查询
        if(invocation.getSqlCmd() == SqlCmd.UPDATE){
            columns.addAll(sqlMeta.getColumns());
            RequestScopeContext requestScope = LoggerContext.getRequestScope();
            String primaryKey = requestScope.getPrimaryKey(sqlMeta.getTableName());
            Column primaryKeyColumn = new Column();
            primaryKeyColumn.setColumnName(primaryKey);

            columns.add(primaryKeyColumn);
        }else{
            //如果是delete，查询所有
            Column column = new Column();
            column.setColumnName("*");
            columns.add(column);
        }
        Expression whereExpression = sqlMeta.getWhereExpression();
        Select select = JsqlParserUtil.getSelect(table, columns, whereExpression);

        //设置select sql
        MappedStatement selectMappedStatement = this.newHashMapMappedStatement(mappedStatement);
        BoundSql queryBoundSql = selectMappedStatement.getBoundSql(invocation.getParameterObject());

        bindBoundSql(queryBoundSql, select.toString());
        //设置where所需要的mapping
        List<ParameterMapping> allMappings = queryBoundSql.getParameterMappings();
        List<ParameterMapping> mappings = getParameterMapping(whereExpression,allMappings);

        bindParameterMappings(queryBoundSql, mappings);

        result.setBoundSql(queryBoundSql);
        result.setMappedStatement(selectMappedStatement);

        return result;
    }


    private List<ParameterMapping> getParameterMapping(Expression whereClause, List<ParameterMapping> allMappings){
        if(allMappings == null || allMappings.size() <= 0){
            return new ArrayList<>();
        }
        List<ParameterMapping> ret = new ArrayList<>();
        if (whereClause != null) {
            int whereSize = (int)whereClause.toString().chars().filter(ch -> ch == '?').count();

            List<ParameterMapping> parameterMappingList = new CopyOnWriteArrayList<>(allMappings);

            return parameterMappingList.subList(allMappings.size()-whereSize,allMappings.size());
        }

        return ret;
    }

    private void bindBoundSql(BoundSql queryBoundSql, String selectSqlString){
        Field field = ReflectionUtils.findField(BoundSql.class, "sql");
        assert field != null;
        field.setAccessible(true);
        ReflectionUtils.setField(field, queryBoundSql, selectSqlString);
    }

    private void bindParameterMappings(BoundSql queryBoundSql, List<ParameterMapping> mappings  ){
        Field field = ReflectionUtils.findField(BoundSql.class, "parameterMappings");
        assert field != null;
        field.setAccessible(true);
        ReflectionUtils.setField(field, queryBoundSql, mappings);
    }


    public  MappedStatement newHashMapMappedStatement(MappedStatement ms) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId() + CUSTOMQUERY, ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), HashMap.class, new ArrayList<>(0)).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }
}