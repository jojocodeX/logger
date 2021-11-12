/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.interceptor;

import lombok.Setter;
import org.aopalliance.aop.Advice;
import org.bravo.logger.core.configuration.Logger;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hejianbing
 * @version @Id: BeanFactoryLoggerAdvisor.java, v 0.1 2021年09月23日 08:36 hejianbing Exp $
 */
@Setter
public class BeanFactoryLoggerAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private Advice      advice;

    private Pointcut    pointcut;

    private BeanFactory beanFactory;

    @PostConstruct
    public void init() {
        this.pointcut = buildPointcut();
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(this.beanFactory);
        }
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private Pointcut buildPointcut() {
        return new AnnotationClassOrMethodOrArgsPointcut();
    }

    private static class AnnotationMethodsResolver {

        private final Class<? extends Annotation> annotationType;

        public AnnotationMethodsResolver(Class<? extends Annotation> annotationType) {
            this.annotationType = annotationType;
        }

        public boolean hasAnnotatedMethods(Class<?> clazz) {
            final AtomicBoolean found = new AtomicBoolean(false);
            ReflectionUtils.doWithMethods(clazz, method -> {
                if (found.get()) {
                    return;
                }
                Annotation annotation = AnnotationUtils.findAnnotation(method,
                    AnnotationMethodsResolver.this.annotationType);
                if (annotation != null) {
                    found.set(true);
                }
            });
            return found.get();
        }

    }

    private final class AnnotationClassOrMethodFilter extends AnnotationClassFilter {

        private final AnnotationMethodsResolver methodResolver;

        AnnotationClassOrMethodFilter(Class<? extends Annotation> annotationType) {
            super(annotationType, true);
            this.methodResolver = new AnnotationMethodsResolver(annotationType);
        }

        @Override
        public boolean matches(Class<?> clazz) {
            return super.matches(clazz) || this.methodResolver.hasAnnotatedMethods(clazz);
        }

    }

    private final class AnnotationClassOrMethodOrArgsPointcut extends DynamicMethodMatcherPointcut {

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return getClassFilter().matches(targetClass);
        }

        @Override
        public ClassFilter getClassFilter() {
            return clazz -> new AnnotationClassOrMethodFilter(Logger.class).matches(clazz);
        }

    }
}