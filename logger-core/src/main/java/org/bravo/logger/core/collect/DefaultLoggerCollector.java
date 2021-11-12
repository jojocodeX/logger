/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.collect;

import org.bravo.logger.core.context.LoggerInfo;
import org.bravo.logger.core.store.StorageLocator;

import java.util.concurrent.Executor;

/**
 * @author hejianbing
 * @version @Id: DefaultLoggerCollector.java, v 0.1 2021年10月25日 10:36 hejianbing Exp $
 */
public class DefaultLoggerCollector implements LoggerCollector {

    private final Executor       executor;

    private final StorageLocator storageLocator;

    public DefaultLoggerCollector(Executor executor, StorageLocator loggerStorage) {
        this.executor = executor;
        this.storageLocator = loggerStorage;
    }

    public Executor getExecute() {
        return executor;
    }

    @Override
    public void collect(LoggerInfo loggerInfo) {
        storageLocator.locate(loggerInfo);
    }
}