/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: LoggerPageResponse.java, v 0.1 2021年10月26日 08:55 hejianbing Exp $
 */
@Setter
@Getter
public class LoggerPageResponse implements Serializable {

    private String               id;

    /** 应用标识 */
    private String               appName;

    /** 操作模块 */
    private String               module;

    /** 操作日志的场景码 */
    private String               sceneCode;

    /** 操作日志的场景描述 */
    private String               sceneName;

    /** 操作日志绑定的业务对象标识 */
    private String               bizNo;

    /** 事件编码*/
    private String               onEvent;

    /** 事件名称 */
    private String               eventName;

    /** 调用方法 */
    private String               method;

    /** 调用目标对象 */
    private String               className;

    /** 操作日志的文本模板 */
    private String               description;

    /** 请求Ip */
    private String               requestIP;

    /** 请求参数信息 */
    private String               params;

    /** 错误信息 */
    private String               errorMessage;

    /** 是否成功 */
    private boolean              success        = true;

    private Object               result;

    /** 用户信息 */
    private String               userNo;

    private String               userName;

    /** 调用时间 毫秒 */
    private Long                 duration;

    /** 开始时间 */
    private Date                 beginDate;

    /** 结束时间 */
    private Date                 endDate;

    /** 方法执行前结果 */
    private String               invokeBefore;

    /** 方法执行后结果 */
    private String               invokeAfter;

    /** 链路Id */
    private String               traceId;

    /** 业务操作信息 */
    private List<ChangeBizContent> bizChangeItems = new ArrayList<>();

}