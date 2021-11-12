/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.commons.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.bravo.logger.commons.utils.UUIDUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author hejianbing
 * @version @Id: LoggerContext.java, v 0.1 2021年09月18日 10:11 hejianbing Exp $
 */
public class LoggerContext {

    private static final TransmittableThreadLocal<Stack<MethodInvokeContext>> methodInvoke  = new TransmittableThreadLocal<Stack<MethodInvokeContext>>(){
        @Override
        protected Stack<MethodInvokeContext> initialValue() {
            return new Stack<>();
        }
    };

    private static final TransmittableThreadLocal<RequestScopeContext>  requestScope = new TransmittableThreadLocal<>();


    public static void addEmptySpan(){
        methodInvoke.get().push(new MethodInvokeContext());
    }

    public static Map<String,Object> getMethodVariables(){
        Map<String, Object> methodContext = methodInvoke.get().peek().getVariable();

        if ( methodContext == null) {
            return new HashMap<>();
        }
        return methodContext;
    }

    public static void removeMethodContext(){
        methodInvoke.get().pop();
    }

    public static MethodInvokeContext getMethodContext(){
        if (methodInvoke.get().isEmpty()) {
            return null;
        }
        
        return methodInvoke.get().peek();
    }


    /** 存放方法调用过程中信息 */
    public static void addSpelModel(String key, String value) {
        if (LoggerContext.methodInvoke.get().isEmpty()) {
            addEmptySpan();
        }
        Stack<MethodInvokeContext> model = methodInvoke.get();

        Map<String,Object> variables = model.peek().getVariable();
        variables.put(key, value);
    }


    public static void getTraceChange() {
        methodInvoke.get().peek().isTraceChange();
    }


    public static RequestScopeContext getRequestScope(){
        if (requestScope.get() == null) {
            return new RequestScopeContext();
        }
        return requestScope.get();
    }

    public static RequestScopeContext nextRequestScope(){
        RequestScopeContext previousScopeContext = requestScope.get();

        RequestScopeContext currentRequestScope = new RequestScopeContext();

        if (previousScopeContext == null) {
            currentRequestScope.setParentId("0");
            currentRequestScope.setSpanId(UUIDUtils.getId());
            currentRequestScope.setTraceId(UUIDUtils.getId());

            requestScope.set(currentRequestScope);

            return currentRequestScope;
        }
        currentRequestScope.setParentId(previousScopeContext.getSpanId());
        currentRequestScope.setTraceId(previousScopeContext.getTraceId());
        currentRequestScope.setSpanId(UUIDUtils.getId());

        return currentRequestScope;
    }

    /**手动调用，需要在调用处进行释放操作 */
    public static void newRequestScope(RequestScopeContext requestScopeContext) {
        if (null != requestScopeContext) {
            RequestScopeContext scopeContext = requestScope.get();

            if (scopeContext != null) {
                return;
            }
            requestScopeContext.setParentId("0");
            if (StringUtils.isBlank(requestScopeContext.getTraceId())) {
                requestScopeContext.setTraceId(UUIDUtils.getId());
            }
            if (StringUtils.isBlank(requestScopeContext.getSpanId())) {
                requestScopeContext.setSpanId(UUIDUtils.getId());
            }
            requestScope.set(requestScopeContext);
        }
    }

    public static void removeRequestScope(){
        if (methodInvoke.get().isEmpty()) {
            requestScope.remove();
        }
    }
}