/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author hejianbing
 * @version @Id: LoggerPageRequest.java, v 0.1 2021年10月26日 08:53 hejianbing Exp $
 */
@Setter
@Getter
public class LoggerPageRequest implements Serializable {

    /** 应用标识 */
    private String appName;

    /** 操作模块 */
    private String module;

    /** 操作日志的场景码 */
    private String sceneCode;

    /** 操作日志的场景描述 */
    private String sceneName;

    /** 操作日志绑定的业务对象标识 */
    private String bizNo;

    /** 请求Ip */
    private String requestIP;

    /** 用户信息 */
    private String userName;

    /** 用户编号 */
    private String userNo;

    /** 调用时间 毫秒 */
    private Long   duration;

    /** 开始时间 */
    private Long   beginDate;

    /** 结束时间 */
    private Long   endDate;

    /** 链路Id */
    private String traceId;
}