/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.service;

import org.bravo.logger.sample.request.UserUpdateRequest;

/**
 * @author hejianbing
 * @version @Id: UserService.java, v 0.1 2021年10月26日 12:32 hejianbing Exp $
 */
public interface UserService {

    Integer update(Integer id,UserUpdateRequest request);
}