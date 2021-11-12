/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.config;

import org.bravo.logger.core.collect.DefaultLoggerCollector;
import org.bravo.logger.core.store.JdbcStorageLocator;
import org.bravo.logger.core.store.StorageLocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.Executors;

/**
 * @author hejianbing
 * @version @Id: MyLoggerConfig.java, v 0.1 2021年10月26日 12:46 hejianbing Exp $
 */
@Configuration
public class MyLoggerConfig {

    @Bean
    @Lazy
    public StorageLocator loggerStorage() {
        return new JdbcStorageLocator();
    }


    @Bean
    public DefaultLoggerCollector collector(StorageLocator loggerStorage) {
        return new DefaultLoggerCollector(Executors.newFixedThreadPool(5), loggerStorage);
    }

}