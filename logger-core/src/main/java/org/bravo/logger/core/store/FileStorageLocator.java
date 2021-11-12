/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.store;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.bravo.logger.core.context.LoggerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: FileLoggerStorage.java, v 0.1 2021年10月25日 10:42 hejianbing Exp $
 */
@Slf4j
public class FileStorageLocator implements StorageLocator {

    public static final String       NULL_STR              = "null";
    public static final String       WRAPPER_CHAR          = "*";
    public static final String       APP_NAME_TITLE        = "应用名称";
    public static final String       SCENE_CODE_TITLE      = "场景编码";
    public static final String       SCENE_NAME_TITLE      = "场景名称";
    public static final String       ACTION_TITLE          = "执行动作";
    public static final String       CLASS_TITLE           = "调用类名";
    public static final String       METHOD_TITLE          = "调用方法";
    public static final String       TRACE_ID_TITLE        = "链路标识";
    public static final String       BUSINESS_ID_TITLE     = "业务标识";
    public static final String       USER_NO_TITLE         = "用户编号";
    public static final String       USER_NAME_TITLE       = "用户姓名";
    public static final String       SUCCESS_TITLE         = "是否成功";
    public static final String       ERROR_MESSAGE_TITLE   = "错误消息";
    public static final String       REQUEST_BODY_TITLE    = "请求参数";
    public static final String       RESULT_TITLE          = "结果返回";
    public static final String       METHOD_DURATION_TITLE = "调用耗时";
    public static final String       SPLITOR               = " : ";
    public static final String       STRING_PLACEHOLDER    = "%s";

    public static final List<String> LOGGER_TITLES = new ArrayList<>();
    static{
        LOGGER_TITLES.add(APP_NAME_TITLE);
        LOGGER_TITLES.add(SCENE_CODE_TITLE);
        LOGGER_TITLES.add(SCENE_NAME_TITLE);
        LOGGER_TITLES.add(TRACE_ID_TITLE);
        LOGGER_TITLES.add(BUSINESS_ID_TITLE);
        LOGGER_TITLES.add(USER_NO_TITLE);
        LOGGER_TITLES.add(USER_NAME_TITLE);
        LOGGER_TITLES.add(ACTION_TITLE);
        LOGGER_TITLES.add(CLASS_TITLE);
        LOGGER_TITLES.add(METHOD_TITLE);
        LOGGER_TITLES.add(REQUEST_BODY_TITLE);
        LOGGER_TITLES.add(RESULT_TITLE);
        LOGGER_TITLES.add(SUCCESS_TITLE);
        LOGGER_TITLES.add(ERROR_MESSAGE_TITLE);
        LOGGER_TITLES.add(METHOD_DURATION_TITLE);
    }

    @Override
    public void locate(LoggerInfo logInfo) {
        StringBuffer messageBuilder = new StringBuffer();
        messageBuilder.append(CharUtils.LF +"************************调用信息 BEGIN*******************************");
        messageBuilder.append(CharUtils.LF);
        String content = addOutputTitle();
        messageBuilder.append(content);

        messageBuilder.append("************************调用信息 END*******************************"+CharUtils.LF);

        String message = String.format(messageBuilder.toString(),
                logInfo.getAppName(),
                StringUtils.isBlank(logInfo.getSceneCode()) ? NULL_STR :logInfo.getSceneCode(),
                StringUtils.isBlank(logInfo.getSceneName()) ? NULL_STR :logInfo.getSceneName(),
                logInfo.getTraceId(),
                logInfo.getBizNo(),
                logInfo.getUserNo(),
                logInfo.getUserName(),
                logInfo.getEvent(),
                logInfo.getClassName(),
                logInfo.getMethod(),
                logInfo.getParams(),
                StringUtils.isBlank(logInfo.getErrorMessage()) ? NULL_STR :logInfo.getErrorMessage(),
                logInfo.isSuccess(),
                null==logInfo.getResult() ? NULL_STR :logInfo.getResult().toString(),
                logInfo.getDuration()+" 毫秒 ");

        if (logInfo.isSuccess()) {
            log.info(message);
            return;
        }
        log.error(message);
    }

    private String addOutputTitle() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String title : LOGGER_TITLES) {
            String content = WRAPPER_CHAR + title + WRAPPER_CHAR + SPLITOR + WRAPPER_CHAR
                    + STRING_PLACEHOLDER + WRAPPER_CHAR + CharUtils.LF;

            stringBuilder.append(content);
        }
        return stringBuilder.toString();
    }

    /*
    public static void main(String[] args) {
        FileStorageLocator storage = new FileStorageLocator();
        SimpleUserProfile simpleUserProfile = new SimpleUserProfile();
        simpleUserProfile.setUserNo("zs");
        simpleUserProfile.setUserName("张三");

        LoggerInfo loggerInfo = new LoggerInfo();
        loggerInfo.setAction(Action.DELETE.getCode());
        loggerInfo.setBizNo("test954444");
        loggerInfo.setClassName("org.bravo.service.UserService");
        loggerInfo.setMethod("save");
        loggerInfo.setSceneCode("test");
        loggerInfo.setSceneName("测试代码");
        loggerInfo.setTraceId("123456");
        loggerInfo.setUserName("xxxxxx");
        loggerInfo.setUserNo("123456");
        loggerInfo.setParams("xxxxxxxx");
        loggerInfo.setResult(null);
        loggerInfo.setDuration(123l);

        storage.locate(loggerInfo);
    }
     */
}