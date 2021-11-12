/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.mybatis.mapped.sqlparser;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.bravo.logger.commons.enums.SqlCmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hejianbing
 * @version @Id: DefaultSqlParser.java, v 0.1 2021年11月11日 22:48 hejianbing Exp $
 */
public class DefaultSqlParser implements SqlParser {

    @Override
    public List<SqlMeta> getSqlMeta(MappedStatement mappedStatement, Object parameter) throws Exception {
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String boundSQL = boundSql.getSql();
        SqlCmd sqlCmd = SqlCmd.ofKey(mappedStatement.getSqlCommandType().name());

        List<SqlMeta> sqlMetaList = new ArrayList<>();

        List<String> sqlList = Arrays.stream(boundSQL.split(";")).collect(Collectors.toList());
        for (String sql : sqlList) {
            sqlMetaList.add(new SqlMeta(sql,sqlCmd));
        }
        return sqlMetaList;
    }

}