/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hejianbing
 * @version @Id: UserUpdateRequest.java, v 0.1 2021年10月26日 12:36 hejianbing Exp $
 */
@Setter
@Getter
public class UserUpdateRequest {
    private String userName;

    private String password;

    private Integer age;

}