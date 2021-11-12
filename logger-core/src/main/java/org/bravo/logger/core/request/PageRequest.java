/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author hejianbing
 * @version @Id: PageRequest.java, v 0.1 2021年10月26日 09:09 hejianbing Exp $
 */
@Setter
@Getter
public class PageRequest<T> implements Serializable {

    private T condition;

    private Long pageSize = 10L;

    private Long currentPage = 1L;
}