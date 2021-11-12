package org.bravo.logger.mybatis.handler;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.schema.Column;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bravo.logger.commons.context.LoggerContext;
import org.bravo.logger.commons.context.RequestScopeContext;
import org.bravo.logger.commons.model.ChangeItem;
import org.bravo.logger.mybatis.mapped.MybatisMethodInvocation;
import org.bravo.logger.mybatis.mapped.SelectMappedStatement;
import org.bravo.logger.mybatis.model.ColumnChangeData;
import org.bravo.logger.mybatis.model.RowChangeData;
import org.bravo.logger.mybatis.mapped.sqlparser.SqlMeta;
import org.bravo.logger.mybatis.util.MybatisUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;

/**
 * @author hejianbing
 * @version @Id: UpdateSqlCmdHandler.java, v 0.1 2021年11月12日 19:50 hejianbing Exp $
 */

@Slf4j
public class UpdateSqlCmdHandler implements SqlCmdHandler {

    @Override
    public void preExecute(MybatisMethodInvocation methodInvocation) throws Exception {
        List<SqlMeta> sqlMetas = methodInvocation.getSqlMetas();

        List<Map<String, Object>> requestParameters = methodInvocation.getRequestParameters();

        List<RowChangeData> result = new ArrayList<>();

        for (int i = 0; i < sqlMetas.size(); i++) {
            SqlMeta sqlMeta = sqlMetas.get(i);
            Expression whereExpression = sqlMeta.getWhereExpression();
            if (whereExpression == null) {
                log.error("更新语句没有where条件！！！");
                continue;
            }

            SelectMappedStatement mappedStatement = methodInvocation.getSelectMappedStatement(sqlMeta);

            //获取更新之前的数据
            List<Map<String, Object>> beforeResults = methodInvocation.getExecutor().queryForMap(
                    mappedStatement.getMappedStatement(),
                    methodInvocation.getParameterObject(),mappedStatement.getBoundSql());

            //获取更新之后的参数中的数据
            Map<String, Object> afterMap1 = requestParameters.get(i);

            //处理这种：update tbl set a = a+1 where id = ?，变更后的值不在参数中而是在表达式中
            List<Map<String, Object>> afterMap2 = getFromSpel(sqlMeta, beforeResults, methodInvocation.isMapUnderscoreToCamelCase());

            //合并afterMap
            List<Map<String, Object>> afterDataMap = combine(afterMap1, afterMap2);

            //组装变更列表
            List<RowChangeData> changeRows = getChangeDataList(methodInvocation.getColumnNameMapping(), afterDataMap, beforeResults, sqlMeta);

            if (CollectionUtils.isNotEmpty(changeRows)) {
                result.addAll(changeRows);
            }
        }

        methodInvocation.setRowChangeData(result);
    }

    private List<RowChangeData> getChangeDataList(final Map<String, String> mapping,
                                                  final List<Map<String, Object>> afterResults,
                                                  final List<Map<String, Object>> beforeResults,
                                                  final SqlMeta sqlMeta) {
        List<RowChangeData> changedDataList = new ArrayList<>();

        RequestScopeContext requestScope = LoggerContext.getRequestScope();

        String primaryKeyName = requestScope.getPrimaryKey(sqlMeta.getTableName());


        if (beforeResults != null && !beforeResults.isEmpty()) {
            for (int i = 0; i < beforeResults.size(); i++) {
                Map<String, Object> beforeDataMap = beforeResults.get(i);

                Map<String, Object> afterDataMap;
                try{
                    afterDataMap = afterResults.get(i) ;
                }catch(Exception ex){
                    afterDataMap = afterResults.get(0);
                }
                RowChangeData changeData = getRowChangeData(primaryKeyName,mapping, afterDataMap, beforeDataMap);

                changeData.setTableName(sqlMeta.getTableName());
                changedDataList.add(changeData);
            }
        }
        return changedDataList;
    }

    private RowChangeData getRowChangeData(final String primaryKeyName,
                                           final Map<String, String> mapping,
                                           final Map<String, Object> afterDataMap,
                                           final Map<String, Object> beforeDataMap) {
        RowChangeData changeData = new RowChangeData();
        List<ColumnChangeData> afterColumnList = new ArrayList<>();
        List<ColumnChangeData> beforeColumnList = new ArrayList<>();
        changeData.setAfterColumnList(afterColumnList);
        changeData.setBeforeColumnList(beforeColumnList);
        if (beforeDataMap == null) {
            return changeData;
        }

        Map<String, ChangeItem> changeColumnMap = new CaseInsensitiveMap<>();

        for (Map.Entry<String, Object> beforeEntry : beforeDataMap.entrySet()) {
            String beforeKey = beforeEntry.getKey().toUpperCase();
            Object beforeValue = beforeEntry.getValue();
            // 保存before
            ColumnChangeData beforeColumn = new ColumnChangeData();
            beforeColumn.setName(beforeKey);
            beforeColumn.setValue(beforeValue);
            beforeColumnList.add(beforeColumn);
            // 保存after
            ColumnChangeData afterColumn = new ColumnChangeData();
            afterColumn.setName(beforeKey);
            afterColumnList.add(afterColumn);

            if (afterDataMap == null) {
                afterColumn.setValue(beforeValue);
            } else {
                //首先从afterMap中查找
                String afterDataName;
                if (afterDataMap.containsKey(beforeKey)) {
                    afterDataName = beforeKey;
                } else {
                    afterDataName = mapping.get(beforeKey);
                }

                boolean isPrimaryKey = false;

                if (primaryKeyName.equalsIgnoreCase(beforeEntry.getKey())) {
                    for (Map.Entry<String, Object> entry : afterDataMap.entrySet()) {
                        if (entry.getValue().equals(beforeValue)) {
                            beforeValue = entry.getValue();
                            afterDataName = entry.getKey();
                            isPrimaryKey = true;
                            break;
                        }
                    }
                }

                if (StringUtils.isBlank(afterDataName)) {
                    log.error("数据库列名字{}没找到对应的值", beforeKey);
                    continue;
                }
                if (afterDataMap.containsKey(afterDataName)) {
                    Object afterValue = afterDataMap.get(afterDataName);

                    afterColumn.setValue(afterValue);
                    //保存change
                    if (beforeValue != null) {
                        if (beforeValue instanceof Date) {
                            beforeValue = toStringDate(beforeValue);
                        }
                    }
                    if (afterValue != null) {
                        if (afterValue instanceof Date) {
                            afterValue = toStringDate(afterValue);
                        }
                        if (afterValue instanceof Boolean) {
                            //before可能是数字 after值是true/false
							// 因为before是从数据库获取的，after是从参数中获取的
                            afterValue = (Boolean) afterValue ? 1 : 0;
                        }
                    }
                    String beforeValueString = beforeValue == null ? "" : beforeValue.toString();
                    String afterValueString = afterValue == null ? "" : afterValue.toString();

                    if (!beforeValueString.equalsIgnoreCase(afterValueString) || isPrimaryKey) {
                        changeColumnMap.put(beforeKey,
                            new ChangeItem(beforeKey, beforeValue, afterValue));
                    }
                }
            }
        }
        //把after中剩余的也添加上，可能是查询条件
        for (Map.Entry<String, Object> entry : afterDataMap.entrySet()) {
            String afterKey = entry.getKey();
            String beforeKey = getBeforeKeyFromMapping(mapping, afterKey);
            String key = beforeKey == null ? afterKey : beforeKey;
            String value = entry.getValue() == null ? "" : entry.getValue().toString();
            if (!beforeDataMap.containsKey(key.toUpperCase())) {
                changeColumnMap.put(key, new ChangeItem(key, value, value));
            }
        }
        //说明只把查询条件放进来了
        if (changeColumnMap.size() <= 1) {
            changeColumnMap.clear();
        }
        changeData.setChangeColumnMap(changeColumnMap);
        return changeData;
    }

    /**
     * 合并两个list
     * @param map1
     * @param list2
     * */
    private List<Map<String, Object>> combine(Map<String, Object> map1,
                                              List<Map<String, Object>> list2) {
        if (map1 == null || map1.size() <= 0) {
            return list2;
        }
        if (list2 == null || list2.size() <= 0) {
            List<Map<String, Object>> ret = new ArrayList<>();
            ret.add(map1);
            return ret;
        }
        List<Map<String, Object>> ret = new ArrayList<>();
        for (Map<String, Object> map2 : list2) {
            map2.putAll(map1);
        }
        return list2;
    }

    /**
     *
     * 从sql语句中解析出变化的数据，比如：
     * sql = "update product set a=a+1,b=100,c=c-1,d=d*10,e=e/10,f=f%10,g=-1,h=0.5,i=a+b where a=100 and b=200 and c=300";
     * Expression
     * a=a+1 Addition(Column LongValue)
     * b=100 LongValue
     * c=c-1 Subtraction
     * d=d*10 Multiplication
     * e=e/10 Division
     * f = f%10 Modulo
     * g = -1 SignedExpresssion
     * h = 0.5 DoubleValue
     * i = a+b Addition(Column Cloumn)
     * @param sqlParserInfo sql
     * @param beforeResults 原始值
     * */
    private List<Map<String, Object>> getFromSpel(SqlMeta sqlParserInfo,
                                                  List<Map<String, Object>> beforeResults,
                                                  boolean mapUnderscoreToCamelCase) {
        if (beforeResults == null) {
            return null;
        }
        List<Map<String, Object>> afterMapList = new ArrayList<>();
        for (Map<String, Object> beforeResult : beforeResults) {
            Map<String, Object> changeMap = new CaseInsensitiveMap<>();

            List<Expression> expressions = sqlParserInfo.getExpressions();
            List<Column> columns = sqlParserInfo.getColumns();
            for (int i = 0; i < expressions.size(); i++) {
                Expression expression = expressions.get(i);
                Column column = columns.get(i);
                String colName = mapUnderscoreToCamelCase
						? MybatisUtils.mapUnderscoreToCamelCase(column.getColumnName()) : column.getColumnName();
                if (expression instanceof StringValue) {
                    changeMap.put(colName, ((StringValue) expression).getValue());
                } else if (expression instanceof LongValue) {
                    changeMap.put(colName, ((LongValue) expression).getValue());
                } else if (expression instanceof DoubleValue) {
                    changeMap.put(colName, ((DoubleValue) expression).getValue());
                } else if (expression instanceof DateValue) {
                    changeMap.put(colName, ((DateValue) expression).getValue());
                } else if (expression instanceof TimeValue) {
                    changeMap.put(colName, ((TimeValue) expression).getValue());
                } else if (expression instanceof TimestampValue) {
                    changeMap.put(colName, ((TimestampValue) expression).getValue());
                } else if (expression instanceof HexValue) {
                    changeMap.put(colName, ((HexValue) expression).getValue());
                } else if (expression instanceof NullValue) {
                    changeMap.put(colName, ((NullValue) expression).toString());
                } else if (expression instanceof SignedExpression) {
                    changeMap.put(colName, ((SignedExpression) expression).toString());
                } else if (expression instanceof Addition) {
                    String ret = calc((Addition) expression, beforeResult,
                        (left, right) -> left.add(right).toPlainString(), mapUnderscoreToCamelCase);

                    changeMap.put(colName, ret);
                } else if (expression instanceof Subtraction) {
                    String ret = calc((Subtraction) expression, beforeResult,
                        (left, right) -> left.subtract(right).toPlainString(), mapUnderscoreToCamelCase);

                    changeMap.put(colName, ret);

                } else if (expression instanceof Multiplication) {
                    String ret = calc((Multiplication) expression, beforeResult,
                            (left, right) -> left.multiply(right).toPlainString(), mapUnderscoreToCamelCase);

                    changeMap.put(colName, ret);
                } else if (expression instanceof Division) {
                    String ret = calc((Division) expression, beforeResult, (left, right) ->
                                    left.divide(right).toPlainString(), mapUnderscoreToCamelCase);
                    changeMap.put(colName, ret);
                } else if (expression instanceof Modulo) {
                    String ret = calc((Modulo) expression, beforeResult, (left, right) -> "" + (left.toBigInteger().longValue() % right.toBigInteger().longValue()),
							mapUnderscoreToCamelCase);
                    changeMap.put(colName, ret);
                }
            }
            if (changeMap.size() > 0) {
                afterMapList.add(changeMap);
            }
        }
        return afterMapList;
    }

    /**
     * 表达式求值，目前仅支持+ - * / %
     * @param binaryExpression 表达式
     * @param beforeResult 列的原始值
     * @param operator
     * @param mapUnderscoreToCamelCase 下划线转驼峰
     * */
    private String calc(BinaryExpression binaryExpression, Map<String, Object> beforeResult,
                        BiFunction<BigDecimal, BigDecimal, String> operator,
                        boolean mapUnderscoreToCamelCase) {
        Expression leftExp = binaryExpression.getLeftExpression();
        Expression rightExp = binaryExpression.getRightExpression();
        Object leftValue = getValue(leftExp, beforeResult, mapUnderscoreToCamelCase);
        Object rightValue = getValue(rightExp, beforeResult, mapUnderscoreToCamelCase);
        if (leftValue == null || rightValue == null) {
            return "";
        }
        BigDecimal leftBd = new BigDecimal(leftValue.toString());
        BigDecimal rightBd = new BigDecimal(rightValue.toString());
        return operator.apply(leftBd, rightBd);
    }

    /**
     * 计算表达式的值，如果是字段，则从查询结果中取，如果是数值类型直接取。
     * @param expression 表达式
     * @param beforeResult 旧的值
     * @param mapUnderscoreToCamelCase 下划线转驼峰
     * */
    private Object getValue(Expression expression, Map<String, Object> beforeResult,
                            boolean mapUnderscoreToCamelCase) {
        //如果是字段名，从查询结果中取值
        if (expression instanceof Column) {
            String colName = ((Column) expression).getColumnName();
            String leftColName = mapUnderscoreToCamelCase
                ? MybatisUtils.mapUnderscoreToCamelCase(colName)
                : colName;

            return beforeResult.get(leftColName);
        } else {
            //否则就是数值
            return expression.toString();
        }
    }

    private String getBeforeKeyFromMapping(Map<String, String> mapping, String key) {
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            String beforeKey = entry.getKey();
            String afterKey = entry.getValue();
            if (afterKey.equalsIgnoreCase(key)) {
                return beforeKey;
            }
        }
        return null;
    }

    private static String toStringDate(Object date) {
        return DateFormatUtils.format((Date) date,
            DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern());
    }
}
