/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.repository;

import org.bravo.logger.core.request.PageRequest;
import org.bravo.logger.core.response.PageResult;
import org.bravo.logger.core.domain.LoggerInfoDomain;
import org.bravo.logger.core.dal.example.LoggerInfoExample;


/**
 * @author hejianbing
 * @version @Id: LoggerRepository.java, v 0.1 2021年10月26日 09:14 hejianbing Exp $
 */
public interface LoggerRepository {

    /**
     * 分页查询
     * @param pageRequest 分页请求对象
     * @return 分页结果
     */
    PageResult<LoggerInfoDomain> pageQuery(PageRequest<LoggerInfoExample> pageRequest);

}