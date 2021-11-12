/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.service;

import org.bravo.logger.sample.request.UserAddressUpdateRequest;

/**
 * @author hejianbing
 * @version @Id: UserAddressService.java, v 0.1 2021年10月26日 13:10 hejianbing Exp $
 */
public interface UserAddressService {

    void update(UserAddressUpdateRequest request);

}