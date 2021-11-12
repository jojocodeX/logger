/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.mybatis.config;

import org.bravo.logger.mybatis.interceptor.MybatisSqlCmdInterceptor;
import org.bravo.logger.mybatis.processor.SqlCmdEmitter;
import org.bravo.logger.mybatis.processor.DefaultSqlCmdEmitter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hejianbing
 * @version @Id: MybatisLoggerConfig.java, v 0.1 2021年10月29日 13:37 hejianbing Exp $
 */
@Configuration
public class MybatisLoggerConfig {

    @Bean
    @ConditionalOnMissingBean
    public SqlCmdEmitter sqlCmdEmitter() {
        return new DefaultSqlCmdEmitter();
    }

    @Bean
    public MybatisSqlCmdInterceptor changeInterceptor(SqlCmdEmitter sqlCmdEmitter) {
        return new MybatisSqlCmdInterceptor(sqlCmdEmitter);
    }
}