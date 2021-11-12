/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.service;

import org.bravo.logger.core.request.PageRequest;
import org.bravo.logger.core.response.PageResult;
import org.bravo.logger.core.request.LoggerPageRequest;
import org.bravo.logger.core.response.LoggerPageResponse;

/**
 * 日志服务
 * @author hejianbing
 * @version @Id: LoggerService.java, v 0.1 2021年10月26日 08:53 hejianbing Exp $
 */
public interface LoggerService {

    PageResult<LoggerPageResponse> pageQuery(PageRequest<LoggerPageRequest> condition);
}