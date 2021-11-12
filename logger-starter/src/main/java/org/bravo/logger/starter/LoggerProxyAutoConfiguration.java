/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.starter;

import lombok.extern.slf4j.Slf4j;
import org.bravo.logger.core.collect.DefaultLoggerCollector;
import org.bravo.logger.core.configuration.LoggerProperties;
import org.bravo.logger.core.dal.support.MySqlPageExecutor;
import org.bravo.logger.core.dal.support.PageExecutor;
import org.bravo.logger.core.interceptor.BeanFactoryLoggerAdvisor;
import org.bravo.logger.core.interceptor.LoggerInterceptor;
import org.bravo.logger.core.service.UserProfileService;
import org.bravo.logger.core.collect.DefaultLoggerInvoker;
import org.bravo.logger.core.collect.LoggerCollector;
import org.bravo.logger.core.collect.LoggerInvoker;
import org.bravo.logger.core.service.impl.LoggerServiceImpl;
import org.bravo.logger.core.store.StorageLocator;
import org.bravo.logger.core.store.FileStorageLocator;
import org.bravo.logger.core.service.LoggerService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * @author hejianbing
 * @version @Id: LoggerProxyAutoConfiguration.java, v 0.1 2021年09月23日 12:41 hejianbing Exp $
 */
@Slf4j
@Configuration
@ImportResource("classpath*:META-INF/spring/module-*.xml")
public class LoggerProxyAutoConfiguration implements ImportAware {

    private AnnotationAttributes enableLog;

    @Value("${spring.application.name:application}")
    private String               serverId;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableLog = AnnotationAttributes
            .fromMap(importMetadata.getAnnotationAttributes(EnableLogger.class.getName(), false));

        if (this.enableLog == null) {
            log.info("@EnableLogger is not present on importing class");
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryLoggerAdvisor beanFactoryLogAdvisor(BeanFactory beanFactory,
                                                          LoggerInterceptor logInterceptor) {
        BeanFactoryLoggerAdvisor advisor = new BeanFactoryLoggerAdvisor();

        advisor.setOrder(this.enableLog.<Integer> getNumber("order"));
        advisor.setAdvice(logInterceptor);
        advisor.setBeanFactory(beanFactory);
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public PageExecutor pageExecutor(JdbcTemplate jdbcTemplate) {
        return new MySqlPageExecutor(jdbcTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public StorageLocator loggerStorage() {
        return new FileStorageLocator();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultLoggerCollector collector(StorageLocator loggerStorage) {
        return new DefaultLoggerCollector(null, loggerStorage);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoggerInvoker loggerInvoker(ObjectProvider<LoggerCollector> collector) {
        return new DefaultLoggerInvoker(collector);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LoggerInterceptor loggerInterceptor(LoggerInvoker loggerInvoker,
                                               ObjectProvider<UserProfileService> userProfileServices) {
        String appName = getAppName();

        LoggerInterceptor interceptor = new LoggerInterceptor();
        interceptor.setAppName(appName);
        interceptor.setCollectorInvoker(loggerInvoker);
        interceptor.setLogUserService(userProfileServices);
        return interceptor;
    }

    @Bean
    public LoggerService loggerService() {
        return new LoggerServiceImpl();
    }

    private String getAppName() {
        String appName = enableLog.getString("appName");

        if (!StringUtils.hasText(appName)) {
            appName = serverId;
        }
        return appName;
    }
}