/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.service.impl;

import org.bravo.logger.commons.context.LoggerContext;
import org.bravo.logger.commons.enums.MethodEvent;
import org.bravo.logger.core.configuration.Logger;
import org.bravo.logger.sample.request.UserAddressUpdateRequest;
import org.bravo.logger.sample.service.UserAddressService;
import org.springframework.stereotype.Component;

/**
 * @author hejianbing
 * @version @Id: UserAddressServiceImpl.java, v 0.1 2021年10月26日 13:20 hejianbing Exp $
 */
@Component
public class UserAddressServiceImpl implements UserAddressService {

    @Logger(description = "用户地址更新请求 {#request.id}",
            bizNo = "{#name}",
            onEvent = MethodEvent.UPDATE)
    @Override
    public void update(UserAddressUpdateRequest request) {

        LoggerContext.addSpelModel("name","zs");

    }
}