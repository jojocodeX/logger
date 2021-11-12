/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.expression;

import java.lang.reflect.Method;

/**
 * @author hejianbing
 * @version @Id: LogExpressionRootObject.java, v 0.1 2021年09月22日 12:45 hejianbing Exp $
 */
public class LogExpressionRootObject {

    private final Method   method;

    private final Object[] args;

    private final Object   target;

    private final Class<?> targetClass;

    public LogExpressionRootObject(Method method, Object[] args, Object target,
                                   Class<?> targetClass) {

        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.args = args;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getMethodName() {
        return this.method.getName();
    }

    public Object[] getArgs() {
        return this.args;
    }

    public Object getTarget() {
        return this.target;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

}