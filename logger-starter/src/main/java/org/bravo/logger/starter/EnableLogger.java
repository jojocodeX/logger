/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.starter;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hejianbing
 * @version @Id: EnableLogger.java, v 0.1 2021年09月23日 12:42 hejianbing Exp $
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LoggerConfigurationSelector.class)
public @interface EnableLogger {

    /** 如果为空获取 spring.application.name;如果spring.application.name为空，默认 application */
    String appName() default "";

    int order() default Ordered.LOWEST_PRECEDENCE;
}
