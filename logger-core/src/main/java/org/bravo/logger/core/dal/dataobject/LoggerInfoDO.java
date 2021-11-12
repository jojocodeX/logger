/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.dal.dataobject;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hejianbing
 * @version @Id: LoggerInfoDO.java, v 0.1 2021年10月26日 09:21 hejianbing Exp $
 */

@Setter
@Getter
public class LoggerInfoDO implements Serializable {

    private String  id;

    /** 应用标识 */
    private String  appName;

    /** 操作模块 */
    private String  module;

    /** 操作日志的场景码 */
    private String  sceneCode;

    /** 操作日志的场景描述 */
    private String  sceneName;

    /** 操作日志绑定的业务对象标识 */
    private String  bizNo;

    /**
     * @see org.bravo.logger.commons.enums.MethodEvent
     */
    private String  methodEvent;

    /** 调用方法 */
    private String  method;

    /** 调用目标对象 */
    private String  className;

    /** 操作日志的文本模板 */
    private String  description;

    /** 请求Ip */
    private String  requestIP;

    /** 请求参数信息 */
    private String  params;

    /** 错误信息 */
    private String  errorMessage;

    /** 是否成功 */
    private boolean success = true;

    private String  result;

    /** 用户信息 */
    private String  userNo;

    /** 用户名 */
    private String  userName;

    /** 调用时间 毫秒 */
    private Long    duration;

    /** 开始时间 */
    private Date    beginDate;

    /** 结束时间 */
    private Date    endDate;

    /** 方法执行前结果 */
    private String  invokeBefore;

    /** 方法执行后结果 */
    private String  invokeAfter;

    /** 链路Id */
    private String  traceId;

    /** 每一条记录信息 */
    private String  spanId;

    /** 父级spanId */
    private String  parentId;

    /**记录业务变更记录 */
    private String  bizChangeItems;
}