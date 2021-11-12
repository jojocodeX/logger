/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.collect;

import org.bravo.logger.core.context.LoggerInfo;

import java.util.concurrent.Executor;

/**
 * @author hejianbing
 * @version @Id: LoggerCollector.java, v 0.1 2021年09月22日 17:34 hejianbing Exp $
 */
public interface LoggerCollector {

    default Executor getExecute() {
        return null;
    }

    void collect(LoggerInfo logMessage);
}