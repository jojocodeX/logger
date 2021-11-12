/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.collect;

import org.bravo.logger.core.context.LoggerInfo;

/**
 * @author hejianbing
 * @version @Id: LogCollectorInvoker.java, v 0.1 2021年09月22日 18:05 hejianbing Exp $
 */
public interface LoggerInvoker {

    void invoke(LoggerInfo logInfo);
}