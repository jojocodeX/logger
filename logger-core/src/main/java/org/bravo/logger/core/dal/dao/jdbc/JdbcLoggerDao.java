/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.dal.dao.jdbc;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.tuple.Pair;
import org.bravo.logger.core.configuration.LoggerProperties;
import org.bravo.logger.core.dal.dao.LoggerDao;
import org.bravo.logger.core.dal.dataobject.LoggerInfoDO;
import org.bravo.logger.core.dal.example.LoggerInfoExample;
import org.bravo.logger.core.dal.support.SqlPageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: JdbcLoggerDao.java, v 0.1 2021年10月26日 09:26 hejianbing Exp $
 */
@Repository
public class JdbcLoggerDao implements LoggerDao{

    @Autowired
    private JdbcTemplate        jdbcTemplate;

    @Autowired
    private LoggerProperties    loggerProperties;

    private static final String INSERT_SQL = "INSERT INTO %s (id,app_name,module,scene_code,scene_name,biz_no,method_event,"
            + "class_name,method,description,request_ip,params,error_message,success,result,user_no,user_name,"
            + "duration,begin_date,end_date,invoke_before,invoke_after,trace_id,span_id,parent_id,biz_change_items)"
            + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


    private static final String COUNT_SQL = "SELECT COUNT(*) FROM %s ";


    public JdbcLoggerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(LoggerInfoDO loggerInfoDO) {
        List<Object> params = new ArrayList<>();
        params.add(loggerInfoDO.getSpanId());
        params.add(loggerInfoDO.getAppName());
        params.add(loggerInfoDO.getModule());
        params.add(loggerInfoDO.getSceneCode());
        params.add(loggerInfoDO.getSceneName());
        params.add(loggerInfoDO.getBizNo());
        params.add(loggerInfoDO.getMethodEvent());
        params.add(loggerInfoDO.getClassName());
        params.add(loggerInfoDO.getMethod());
        params.add(loggerInfoDO.getDescription());
        params.add(loggerInfoDO.getRequestIP());
        params.add(loggerInfoDO.getParams());
        params.add(loggerInfoDO.getErrorMessage());
        params.add(loggerInfoDO.isSuccess());
        params.add(loggerInfoDO.getResult());
        params.add(loggerInfoDO.getUserNo());
        params.add(loggerInfoDO.getUserName());
        params.add(loggerInfoDO.getDuration());
        params.add(new Timestamp(loggerInfoDO.getBeginDate().getTime()));
        params.add(new Timestamp(loggerInfoDO.getEndDate().getTime()));
        params.add(loggerInfoDO.getInvokeBefore());
        params.add(loggerInfoDO.getInvokeAfter());
        params.add(loggerInfoDO.getTraceId());
        params.add(loggerInfoDO.getSpanId());
        params.add(loggerInfoDO.getParentId());
        params.add(loggerInfoDO.getBizChangeItems());

        int rows = jdbcTemplate.update(String.format(INSERT_SQL,loggerProperties.getTableName()), params.toArray(new Object[params.size()]));

        Preconditions.checkArgument(rows > 0, "jdbc store system.error");

        return rows;
    }

    @Override
    public Long count(LoggerInfoExample condition) {
        SqlPageBuilder sqlPageBuilder = new SqlPageBuilder(condition);

        Pair<String, List<Object>> pageCondition = sqlPageBuilder.getPageCondition();

        StringBuilder SQL = new StringBuilder();

        SQL.append(String.format(COUNT_SQL,loggerProperties.getTableName()));
        SQL.append(pageCondition.getLeft());

       return jdbcTemplate.queryForObject(SQL.toString(), Long.class, pageCondition.getRight().toArray(new Object[pageCondition.getRight().size()]));
    }

}