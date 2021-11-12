/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.expression;

import org.bravo.logger.commons.context.LoggerContext;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author hejianbing
 * @version @Id: LogEvaluationContext.java, v 0.1 2021年09月18日 10:10 hejianbing Exp $
 */
public class LogEvaluationContext  extends MethodBasedEvaluationContext {

    public LogEvaluationContext(Object rootObject, Method method, Object[] arguments,
                                ParameterNameDiscoverer parameterNameDiscoverer, Object result, String error) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
        Map<String, Object> variables = LoggerContext.getMethodVariables();
        if (!variables.isEmpty()) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                setVariable(entry.getKey(), entry.getValue());
            }
        }
        setVariable("result", result);
        setVariable("error", error);
    }

}