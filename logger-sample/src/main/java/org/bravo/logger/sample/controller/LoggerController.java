/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.controller;

import org.bravo.logger.core.request.LoggerPageRequest;
import org.bravo.logger.core.request.PageRequest;
import org.bravo.logger.core.response.LoggerPageResponse;
import org.bravo.logger.core.response.PageResult;
import org.bravo.logger.core.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hejianbing
 * @version @Id: LoggerController.java, v 0.1 2021年10月28日 13:36 hejianbing Exp $
 */
@RestController
@RequestMapping("logger")
public class LoggerController {

    @Autowired(required = false)
    private LoggerService loggerService;


    @PostMapping("page")
    public @ResponseBody
    PageResult<LoggerPageResponse> pageExecute(@RequestBody PageRequest<LoggerPageRequest> pageRequest) {
        return loggerService.pageQuery(pageRequest);

    }
}