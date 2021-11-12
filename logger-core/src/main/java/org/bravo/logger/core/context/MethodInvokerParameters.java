/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.context;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hejianbing
 * @version @Id: MethodInvokerParameters.java, v 0.1 2021年09月23日 12:18 hejianbing Exp $
 */
@Setter
@Getter
public class MethodInvokerParameters {

    private String parameterName;
    private String parameterTypeName;
    private Object argument;
}