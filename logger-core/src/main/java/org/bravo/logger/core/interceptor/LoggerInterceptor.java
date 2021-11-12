/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.bravo.logger.core.configuration.Logger;
import org.bravo.logger.commons.context.MethodInvokeContext;
import org.bravo.logger.commons.context.RequestScopeContext;
import org.bravo.logger.commons.context.LoggerContext;
import org.bravo.logger.core.context.LoggerInfo;
import org.bravo.logger.core.context.MethodInvokerParameters;
import org.bravo.logger.core.init.DataSourceMetadataInitializer;
import org.bravo.logger.core.request.UserProfile;
import org.bravo.logger.core.expression.SpELResolverTemplate;
import org.bravo.logger.core.service.UserProfileService;
import org.bravo.logger.core.collect.LoggerInvoker;
import org.slf4j.LoggerFactory;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 参考  org.springframework.cloud.sleuth.annotation.SleuthAdvisorConfig 实现
 *
 * @author hejianbing
 * @version @Id: LoggerInterceptor.java, v 0.1 2021年09月18日 09:35 hejianbing Exp $
 */
@Setter
@Getter
public class LoggerInterceptor implements BeanFactoryAware, IntroductionInterceptor{

    private final org.slf4j.Logger LOGGER = LoggerFactory
        .getLogger(LoggerInterceptor.class);

    //获取到Spring容器的beanFactory对象
    private BeanFactory                        beanFactory;

    private LoggerInvoker                      collectorInvoker;

    private ObjectProvider<UserProfileService> logUserService;

    private String                             appName;

    @Autowired
    private DataSourceMetadataInitializer dataSourceMetadataInitializer;



    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable  {
        Method method = invocation.getMethod();

        Method mostSpecificMethod = AopUtils.getMostSpecificMethod(method, invocation.getThis().getClass());
        Logger LOG =  AnnotationUtils.findAnnotation(mostSpecificMethod, Logger.class);

        if (LOG == null) {
            return invocation.proceed();
        }

        LoggerContext.addEmptySpan();

        LoggerInfo loggerInfo = this.doBeforeInvoke(invocation,LOG);

        Object result = null;

        StopWatch stopWatch = new StopWatch(invocation.getMethod().getName());
        stopWatch.start();

        try {
            result = invocation.proceed();

            loggerInfo.setResult(result);
        } catch (Exception ex) {
            loggerInfo.setErrorMessage(ex.getMessage());
            loggerInfo.setSuccess(false);

            LOGGER.error("调用 {},方法 {},参数 {},出现异常:", invocation.getThis().getClass().getSimpleName(),
                mostSpecificMethod.getName(), invocation.getArguments());
            throw ex;

        } finally {
            stopWatch.stop();
            loggerInfo.setDuration(stopWatch.getTotalTimeMillis());

            loggerInfo.setEndDate(new Date());

            this.collectExecute(loggerInfo,
                    method,
                    invocation.getThis(),
                    invocation.getArguments(),
                    LOG);
        }
        return result;
    }

    @Override
    public boolean implementsInterface(Class<?> intf) {
        return true;
    }


    //~~~ 内部方法


    private LoggerInfo doBeforeInvoke(MethodInvocation invocation, Logger log) {
        LoggerInfo loggerInfo = new LoggerInfo();

        RequestScopeContext requestScope = LoggerContext.nextRequestScope();

        requestScope.setTableMeta(dataSourceMetadataInitializer.getTableMeta());

        loggerInfo.setTraceId(requestScope.getTraceId());
        loggerInfo.setSpanId(requestScope.getSpanId());
        loggerInfo.setParentId(requestScope.getParentId());

        MethodInvokeContext methodContext = LoggerContext.getMethodContext();
        methodContext.setTraceChange(log.traceChange());

        String beforeMethod = log.beforeExecute();

        SpELResolverTemplate spElTemplates = getSpELTemplate(invocation.getMethod(),
            invocation.getThis(), invocation.getArguments(), null, null);

        String beforeResult = spElTemplates.resolveJSONString(beforeMethod);

        loggerInfo.setInvokeBefore(beforeResult);
        loggerInfo.setBeginDate(new Date());

        return loggerInfo;
    }

    private void collectExecute(LoggerInfo logInfo,
                                Method method,
                                Object aThis,
                                Object[] arguments,
                                Logger LOG) {
        try{
            SpELResolverTemplate spElTemplates =getSpELTemplate(method,aThis,arguments,logInfo.getResult(),logInfo.getMethod());

            // 获取用户信息
            UserProfile userProfile = this.getContextUser(spElTemplates,LOG);

            // 获取请求参数
            String parameters = this.getMethodParameters(method,arguments);

            // 调用后对象
            String afterResult = spElTemplates.resolveJSONString(LOG.afterExecute());

            logInfo.setInvokeAfter(afterResult);

            logInfo.setDescription(spElTemplates.resolveJSONString(LOG.description()));
            logInfo.setBizNo(spElTemplates.resolveJSONString(LOG.bizNo()));
            logInfo.setSceneCode(LOG.sceneCode());
            logInfo.setSceneName(LOG.sceneName());
            logInfo.setEvent(LOG.onEvent().getCode());
            logInfo.setModule(LOG.module());

            logInfo.setUserNo(userProfile.getUserNo());
            logInfo.setUserName(userProfile.getUserName());
            logInfo.setClientIp(LoggerContext.getRequestScope().getIp());

            logInfo.setAppName(appName);
            logInfo.setParams(parameters);

            logInfo.setClassName(aThis.getClass().getName());
            logInfo.setMethod(method.getName());

            logInfo.setBizChangeItems(LoggerContext.getMethodContext().getBizChangeItems());

            collectorInvoker.invoke(logInfo);
        }catch(Exception ex){
            // 日志出错，不影响业务
            LOGGER.error("log parse exception", ex);
        }finally{
            LoggerContext.removeMethodContext();

            LoggerContext.removeRequestScope();

        }
    }


    private String getMethodParameters(Method method, Object[] arguments) {
        List<MethodInvokerParameters> parameters = new ArrayList<>();

        for (int i = 0; i < arguments.length; i++) {
            MethodParameter methodParameter = new MethodParameter(method,i);

            methodParameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());

            String parameterName = methodParameter.getParameterName();
            String simpleName = methodParameter.getParameterType().getSimpleName();
            Object argument = arguments[i];

            MethodInvokerParameters invokerParameters = new MethodInvokerParameters();
            invokerParameters.setParameterName(parameterName);
            invokerParameters.setParameterTypeName(simpleName);
            invokerParameters.setArgument(argument);

            parameters.add(invokerParameters);
        }

        if (CollectionUtils.isEmpty(parameters)) {
            return null;
        }
        return JSON.toJSONString(parameters);
    }

    // 获取用户信息
    private UserProfile getContextUser(SpELResolverTemplate spElTemplates, Logger log) {
        if (StringUtils.hasText(log.identity())) {
            UserProfile simpleUserProfile = new UserProfile();

            String userInfo = spElTemplates
                .resolveJSONString(log.identity() + "," + log.userName());

            String[] user = userInfo.split(",");
            simpleUserProfile.setUserNo(user[0]);

            if (StringUtils.hasText(log.userName())) {
                simpleUserProfile.setUserName(user[1]);
            }
            return simpleUserProfile;
        }
        UserProfileService userService = logUserService.getIfAvailable();

        if (userService == null) {
            throw new RuntimeException("没有提供日志用户上下文服务");
        }
        UserProfile user = userService.getUser();
        if (user == null) {
            throw new RuntimeException("当前上下文未找到用户会话信息");
        }
        return user;
    }

    private SpELResolverTemplate getSpELTemplate(Method method, Object aThis, Object[] arguments, Object result, String error){
        return SpELResolverTemplate.builder()
                .beanFactory(beanFactory)
                .method(method)
                .target(aThis)
                .args(arguments)
                .result(result)
                .error(error)
                .build();
    }

}