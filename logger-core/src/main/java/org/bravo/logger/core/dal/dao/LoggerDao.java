/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.dal.dao;

import org.bravo.logger.core.dal.dataobject.LoggerInfoDO;
import org.bravo.logger.core.dal.example.LoggerInfoExample;

/**
 * @author hejianbing
 * @version @Id: LoggerJdbc.java, v 0.1 2021年10月26日 09:25 hejianbing Exp $
 */
public interface LoggerDao {

    /**
     * 插入日志
     * @param loggerInfoDO
     * @return 影响db行数
     */
    int insert(LoggerInfoDO loggerInfoDO);

    /**
     * 统计分页条数
     * @param condition 查询条件
     * @return 统计条数
     */
    Long count(LoggerInfoExample condition);

}