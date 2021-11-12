/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.starter;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author hejianbing
 * @version @Id: LoggerConfigurationSelector.java, v 0.1 2021年10月28日 14:31 hejianbing Exp $
 */
public class LoggerConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importMetadata) {
        AnnotationAttributes attr = AnnotationAttributes
            .fromMap(importMetadata.getAnnotationAttributes(EnableLogger.class.getName(), false));

        String[] names = new String[0];

        if (attr == null) {
            return names;
        }
        return new String[] {LoggerProxyAutoConfiguration.class.getName() };
    }
}