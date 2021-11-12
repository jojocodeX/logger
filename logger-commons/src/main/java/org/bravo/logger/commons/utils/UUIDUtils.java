/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.commons.utils;

import org.springframework.util.AlternativeJdkIdGenerator;

/**
 * @author hejianbing
 * @version @Id: LoggerIdUtils.java, v 0.1 2021年10月26日 17:37 hejianbing Exp $
 */
public class UUIDUtils {

    public static String getId(){
        return new AlternativeJdkIdGenerator().generateId().toString().replaceAll("-", "");
    }
}