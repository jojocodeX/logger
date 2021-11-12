/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hejianbing
 * @version @Id: LoggerProperties.java, v 0.1 2021年10月28日 09:18 hejianbing Exp $
 */
@Component
@ConfigurationProperties(prefix = "logger")
@Setter
@Getter
public class LoggerProperties {

    /** 表名 */
    private String tableName = "sys_biz_log";
}