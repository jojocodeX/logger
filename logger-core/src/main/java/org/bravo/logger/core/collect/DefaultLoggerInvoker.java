/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.collect;

import org.bravo.logger.core.context.LoggerInfo;
import org.springframework.beans.factory.ObjectProvider;

import java.util.concurrent.Executor;

/**
 * @author hejianbing
 * @version @Id: DefaultLoggerInvoker.java, v 0.1 2021年09月22日 18:14 hejianbing Exp $
 */
public class DefaultLoggerInvoker implements LoggerInvoker {

    private final ObjectProvider<LoggerCollector> loggerCollector;

    public DefaultLoggerInvoker(ObjectProvider<LoggerCollector> loggerCollector) {
        this.loggerCollector = loggerCollector;
    }

    @Override
    public void invoke(LoggerInfo logInfo) {
        loggerCollector.ifAvailable(collector->{
            Executor execute = collector.getExecute();
            if (execute != null) {
                execute.execute(()-> doInvoke(logInfo));
            }else{
                doInvoke(logInfo);
            }
        });
    }

    private void doInvoke(LoggerInfo logMessage){
        loggerCollector.ifAvailable(collector->{
            collector.collect(logMessage);
        });
    }
}