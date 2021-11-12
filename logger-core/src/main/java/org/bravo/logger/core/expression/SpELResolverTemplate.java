/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.expression;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bravo.logger.commons.utils.ClassMatchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;

/**
 * @author hejianbing
 * @version @Id: SpELResolverTemplate.java, v 0.1 2021年09月23日 09:02 hejianbing Exp $
 */
public class SpELResolverTemplate {

    private final Logger                 LOG                    = LoggerFactory
        .getLogger(SpELResolverTemplate.class);
    private final LogExpressionEvaluator logExpressionEvaluator = new LogExpressionEvaluator();
    private final BeanFactory            beanFactory;
    private final Method                 method;
    private final Class<?>               targetClass;
    private final EvaluationContext      evaluationContext;
    private final DefaultConversionService conversionService = new DefaultConversionService();;
    public static LogSpELTemplateBuilder templateBuilder        = new LogSpELTemplateBuilder();

    public static LogSpELTemplateBuilder builder() {
        return templateBuilder;
    }

    private SpELResolverTemplate(BeanFactory beanFactory, Method method, Object[] args,
                                 Object target, Class<?> targetClass, Object result, String error) {
        this.beanFactory = beanFactory;
        this.targetClass = targetClass;
        this.method = method;

        this.evaluationContext = logExpressionEvaluator.createEvaluationContext(beanFactory, target,
            targetClass, method, args, result, error);
    }

    public String resolveJSONString(String conditionExpression) {
        Object result = resolve(conditionExpression);

        if (StringUtils.isBlank(ObjectUtils.toString(result, "")) || result == null ) {
            return null;
        }

        if (ClassMatchUtils.isPrimitiveOrWrapper(result.getClass())) {
            return conversionService.convert(result, String.class);
        }
        return JSON.toJSONString(result);
    }

    public <T> T resolve(String conditionExpression) {
        if (StringUtils.isBlank(conditionExpression)) {
            return null;
        }

        Object result = logExpressionEvaluator.parseExpression(
            resolvePlaceholders(conditionExpression), new AnnotatedElementKey(method, targetClass),
            evaluationContext);

        return (T) result;
    }

    private String resolvePlaceholders(String value) {
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value);
        }
        return value;
    }

    public static class LogSpELTemplateBuilder {
        private BeanFactory beanFactory;
        private Method      method;
        private Object      target;
        private Object[]    args;
        private Object      result;
        private String      error;

        public LogSpELTemplateBuilder beanFactory(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
            return this;
        }

        public LogSpELTemplateBuilder method(Method method) {
            this.method = method;
            return this;
        }

        public LogSpELTemplateBuilder target(Object target) {
            this.target = target;
            return this;
        }

        public LogSpELTemplateBuilder args(Object[] args) {
            this.args = args;
            return this;
        }

        public LogSpELTemplateBuilder result(Object result) {
            this.result = result;
            return this;
        }

        public LogSpELTemplateBuilder error(String error) {
            this.error = error;
            return this;
        }

        public SpELResolverTemplate build() {
            return new SpELResolverTemplate(this.beanFactory, method, args, target,
                AopProxyUtils.ultimateTargetClass(target), result, error);
        }

    }

}