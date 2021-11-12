/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.expression;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hejianbing
 * @version @Id: LogExpressionEvaluator.java, v 0.1 2021年09月18日 10:04 hejianbing Exp $
 */
public class LogExpressionEvaluator extends CachedExpressionEvaluator {

    private final Map<LogExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

    private final Map<AnnotatedElementKey, Method>  targetMethodCache = new ConcurrentHashMap<>(64);

    private ParserContext                     parserContext     = new TemplateParserContext("{", "}");


    public EvaluationContext createEvaluationContext(BeanFactory beanFactory,
                                                     Object target,
                                                     Class<?> targetClass,
                                                     Method method,
                                                     Object[] args,
                                                     Object result,
                                                     String error) {

        Method targetMethod = getTargetMethod(targetClass, method);
        LogExpressionRootObject rootObject = new LogExpressionRootObject(method,args,target,targetClass);
        LogEvaluationContext evaluationContext = new LogEvaluationContext(rootObject,method,args,new DefaultParameterNameDiscoverer(),result,error);
        evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));

        return new LogEvaluationContext(rootObject, targetMethod, args, new DefaultParameterNameDiscoverer(),result,error);
    }

    public Object parseExpression(String conditionExpression, AnnotatedElementKey methodKey,
                                  EvaluationContext evalContext) {
        return getLogExpression(this.expressionCache, methodKey, conditionExpression)
            .getValue(evalContext);
    }

    protected Expression getLogExpression(Map<LogExpressionKey, Expression> cache,
                                       AnnotatedElementKey elementKey, String expression) {

        LogExpressionKey expressionKey = new LogExpressionKey(elementKey, expression);
        Expression expr = cache.get(expressionKey);
        if (expr == null) {
            expr = getParser().parseExpression(expression, parserContext);
            cache.put(expressionKey, expr);
        }
        return expr;

    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }

}