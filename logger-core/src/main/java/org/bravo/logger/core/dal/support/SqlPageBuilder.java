/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.dal.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bravo.logger.core.dal.example.LoggerInfoExample;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: SqlPageBuilder.java, v 0.1 2021年10月28日 09:00 hejianbing Exp $
 */
public class SqlPageBuilder {

    private LoggerInfoExample condition;

    public SqlPageBuilder(LoggerInfoExample condition) {
        this.condition = condition;
    }

    public Pair<String, List<Object>> getPageCondition(){
        StringBuffer sqlBuilder = new StringBuffer();
        List<Object> params = new ArrayList<>();
        params.add(condition.getAppName());

        sqlBuilder.append("WHERE app_name like '%' ? '%' ");

        if (StringUtils.isNotBlank(condition.getModule())) {
            sqlBuilder.append("and module like '%' ? '%'");
            params.add(condition.getModule());
        }

        if (StringUtils.isNotBlank(condition.getSceneCode())) {
            sqlBuilder.append("and scene_code = ? ");
            params.add(condition.getSceneCode());
        }

        if (StringUtils.isNotBlank(condition.getSceneName())) {
            sqlBuilder.append("and scene_name like '%' ? '%'");
            params.add(condition.getSceneName());
        }

        if (StringUtils.isNotBlank(condition.getBizNo())) {
            sqlBuilder.append("and biz_no like '%' ? '%' ");
            params.add(condition.getBizNo());
        }

        if (StringUtils.isNotBlank(condition.getRequestIP())) {
            sqlBuilder.append("and request_ip = ? ");
            params.add(condition.getRequestIP());
        }

        if (StringUtils.isNotBlank(condition.getUserName())) {
            sqlBuilder.append("and user_name like '%' ? '%' ");
            params.add(condition.getUserName());
        }

        if (StringUtils.isNotBlank(condition.getUserNo())) {
            sqlBuilder.append("and user_no = ? ");
            params.add(condition.getUserNo());
        }

        if (null != condition.getDuration()) {
            sqlBuilder.append("and duration >= ? ");
            params.add(condition.getDuration());
        }
        if (StringUtils.isNotBlank(condition.getTraceId())) {
            sqlBuilder.append("and trace_id = ? ");
            params.add(condition.getTraceId());
        }

        if (condition.getBeginDate() != null && condition.getEndDate() != null) {
            sqlBuilder.append("and begin_date >= ? and end_date <= ? ");

            params.add(new Date(condition.getBeginDate().getTime()));
            params.add(new Date(condition.getEndDate().getTime()));
        }

        if (condition.getBeginDate() != null && condition.getEndDate() == null) {
            sqlBuilder.append("and begin_date >= ? ");
            params.add(new Date(condition.getBeginDate().getTime()));
        }

        if (condition.getEndDate() != null && condition.getBeginDate() == null) {
            sqlBuilder.append("and end_date <= ? ");
            params.add(new Date(condition.getEndDate().getTime()));
        }
        return Pair.of(sqlBuilder.toString(), params);
    }
}