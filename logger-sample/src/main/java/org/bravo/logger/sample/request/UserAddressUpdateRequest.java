/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hejianbing
 * @version @Id: UserAddressUpdateRequest.java, v 0.1 2021年10月26日 13:19 hejianbing Exp $
 */
@Setter
@Getter
public class UserAddressUpdateRequest {

    private String zipCode;

    private Integer id;
}