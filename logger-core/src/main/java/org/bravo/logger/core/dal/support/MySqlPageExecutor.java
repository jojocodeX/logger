/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.dal.support;

import org.apache.commons.lang3.tuple.Pair;
import org.bravo.logger.core.configuration.LoggerProperties;
import org.bravo.logger.core.dal.dataobject.LoggerInfoDO;
import org.bravo.logger.core.dal.example.LoggerInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author hejianbing
 * @version @Id: MySqlPageExecutor.java, v 0.1 2021年10月28日 08:50 hejianbing Exp $
 */
public class MySqlPageExecutor implements PageExecutor {

    private final JdbcTemplate  jdbcTemplate;

    @Autowired
    private LoggerProperties    loggerProperties;

    private static final String PAGE_SELECT = " SELECT * FROM %s ";

    public MySqlPageExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<LoggerInfoDO> pageByCondition(Long currentPage, Long pageSize,
                                              LoggerInfoExample condition) {
        SqlPageBuilder sqlPageBuilder = new SqlPageBuilder(condition);

        Pair<String, List<Object>> pageCondition = sqlPageBuilder.getPageCondition();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(String.format(PAGE_SELECT,loggerProperties.getTableName()));
        sqlBuilder.append(pageCondition.getLeft());
        sqlBuilder.append(" LIMIT ?,? ");

        List<Object> params = pageCondition.getRight();
        params.add(currentPage);
        params.add(pageSize);

        List<LoggerInfoDO> result =jdbcTemplate.query(sqlBuilder.toString(), params.toArray(new Object[params.size()]), (rs, rowNum) -> {

            LoggerInfoDO loggerInfoDO = new LoggerInfoDO();

            loggerInfoDO.setId(rs.getString("id"));
            loggerInfoDO.setAppName(rs.getString("app_name"));
            loggerInfoDO.setModule(rs.getString("module"));
            loggerInfoDO.setSceneCode(rs.getString("scene_code"));
            loggerInfoDO.setSceneName(rs.getString("scene_name"));
            loggerInfoDO.setBizNo(rs.getString("biz_no"));
            loggerInfoDO.setMethodEvent(rs.getString("method_event"));
            loggerInfoDO.setMethod(rs.getString("method"));
            loggerInfoDO.setClassName(rs.getString("class_name"));
            loggerInfoDO.setDescription(rs.getString("description"));
            loggerInfoDO.setRequestIP(rs.getString("request_ip"));
            loggerInfoDO.setParams(rs.getString("params"));
            loggerInfoDO.setErrorMessage(rs.getString("error_message"));
            loggerInfoDO.setSuccess(rs.getBoolean("success"));
            loggerInfoDO.setResult(rs.getString("result"));
            loggerInfoDO.setUserNo(rs.getString("user_no"));
            loggerInfoDO.setUserName(rs.getString("user_name"));
            loggerInfoDO.setDuration(rs.getLong("duration"));
            loggerInfoDO.setBeginDate(new java.util.Date(rs.getTimestamp("begin_date").getTime()));
            loggerInfoDO.setEndDate(new java.util.Date(rs.getTimestamp("end_date").getTime()));
            loggerInfoDO.setInvokeBefore(rs.getString("invoke_before"));
            loggerInfoDO.setInvokeAfter(rs.getString("invoke_after"));
            loggerInfoDO.setTraceId(rs.getString("trace_id"));
            loggerInfoDO.setSpanId(rs.getString("span_id"));
            loggerInfoDO.setParentId(rs.getString("parent_id"));
            loggerInfoDO.setBizChangeItems(rs.getString("biz_change_items"));

            return loggerInfoDO;
        });

        return result;
    }
}