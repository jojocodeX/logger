/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.dal.support;

import org.bravo.logger.core.dal.dataobject.LoggerInfoDO;
import org.bravo.logger.core.dal.example.LoggerInfoExample;

import java.util.List;

/**
 * @author hejianbing
 * @version @Id: PageExecutor.java, v 0.1 2021年10月28日 08:48 hejianbing Exp $
 */
public interface PageExecutor {

    /**
     * 分页查询
     * @param currentPage 当前页
     * @param pageSize 分页条数
     * @param condition 查询条件
     * @return 查询返回结果
     */
    List<LoggerInfoDO> pageByCondition(Long currentPage, Long pageSize, LoggerInfoExample condition);
}