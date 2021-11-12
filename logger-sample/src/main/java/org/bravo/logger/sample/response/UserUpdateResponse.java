/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hejianbing
 * @version @Id: UserUpdateResponse.java, v 0.1 2021年10月26日 13:00 hejianbing Exp $
 */
@Setter
@Getter
public class UserUpdateResponse {

    private String userName;
    private String password;
    private Integer age;
    private Integer id;
}