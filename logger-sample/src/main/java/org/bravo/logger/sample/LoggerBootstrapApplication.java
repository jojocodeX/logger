/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample;

import org.bravo.logger.starter.EnableLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author hejianbing
 * @version @Id: LoggerBootstrapApplication.java, v 0.1 2021年10月26日 12:26 hejianbing Exp $
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableLogger
public class LoggerBootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggerBootstrapApplication.class, args);
    }
}