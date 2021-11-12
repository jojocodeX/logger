/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.configuration;

import org.bravo.logger.commons.enums.MethodEvent;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hejianbing
 * @version @Id: Log.java, v 0.1 2021年09月18日 09:24 hejianbing Exp $
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logger {

    /** 方法执行前 */
    String beforeExecute() default "";

    /** 方法执行后*/
    String afterExecute() default "";

    /** 如果设置会 true 在操作当前方法内所有更新操作时，会记录变更前后的数据
     *
     *主要参考： https://github.com/xjs1919/data-audit/blob/master/data-audit-sdk/src/main/java/com/github/xjs/audit/mybatis/parser/UpdateParser.java
     * https://www.xiaoheidiannao.com/116293.html
     * https://gitee.com/lopssh/Mybatis-Auditlog-Plugin/blob/master/src/main/java/com/gitee/lopssh/plugin/mybatis/auditlog/interceptor/SQLAuditLogInterceptor.java
     https://gitee.com/lopssh/Mybatis-Auditlog-Plugin/blob/master/src/main/java/com/gitee/lopssh/plugin/mybatis/auditlog/interceptor/handler/MySqlUpdateSQLAuditHandler.java
     */
    boolean traceChange() default false;

    /** 操作模块 */
    String module() default "";

    /** 操作日志的场景码，默认为方法名 */
    String sceneCode() default "";

    /** 操作日志的场景描述 */
    String sceneName() default "";

    /** 操作日志的执行人 */
    String identity() default "";

    /** 操作日志的执行人用户名称 */
    String userName() default "";

    /** 操作日志绑定的业务对象标识 */
    String bizNo() default "";

    /** 方法事件 */
    MethodEvent onEvent();

    /** 操作日志的文本模板 */
    String description();
}
