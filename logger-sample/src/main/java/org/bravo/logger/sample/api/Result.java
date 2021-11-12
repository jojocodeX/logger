/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.api;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hejianbing
 * @version @Id: Result.java, v 0.1 2021年10月26日 12:58 hejianbing Exp $
 */
@Setter
@Getter
public class Result<T> {

    private T       data;

    private boolean success;

    private String  error;
}