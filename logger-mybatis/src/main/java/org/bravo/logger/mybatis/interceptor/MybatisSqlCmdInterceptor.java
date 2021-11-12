/** 
 * copyright(c) 2019-2029 mamcharge.com
 */
 
package org.bravo.logger.mybatis.interceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.bravo.logger.commons.context.LoggerContext;
import org.bravo.logger.mybatis.mapped.MybatisMethodInvocation;
import org.bravo.logger.mybatis.processor.SqlCmdEmitter;
import org.bravo.logger.mybatis.handler.SqlCmdHandler;
import org.bravo.logger.mybatis.handler.SqlCmdHandleManager;

import java.util.Properties;

/**
 * @author hejianbing
 * @Id: MybatisSqlCmdInterceptor.java, v 0.1 2021年10月29日 14:08 hejianbing Exp $
 *
 */
@Slf4j
@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MybatisSqlCmdInterceptor implements Interceptor {

    private final SqlCmdEmitter sqlCmdEmitter;

    public MybatisSqlCmdInterceptor(SqlCmdEmitter sqlCmdEmitter){
        this.sqlCmdEmitter = sqlCmdEmitter;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截目标
        Object target = invocation.getTarget();
        Object result = null;
        if (target instanceof Executor) {
            if (!isSkip()) {
                return invocation.proceed();
            }
            Executor executor = (Executor) target;
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = args[1];

            MybatisMethodInvocation methodInvocation = new MybatisMethodInvocation(executor, ms, parameter);

            SqlCmdHandler sqlCmdHandler = SqlCmdHandleManager.getSqlCmd(methodInvocation.getSqlCmd());

            boolean success = beforeInvoke(methodInvocation, sqlCmdHandler);

            // 2. 执行Update方法，除了查询之外的Insert，Delete，Update都是属于Update方法
            result = invocation.proceed();

            // 3. 方法执行之后处理数据,方法执行成功才需要记录差量
            this.afterInvoke(success,sqlCmdHandler,methodInvocation);
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }



    private boolean isSkip(){
        if (LoggerContext.getMethodContext()==null) {
            return false;
        }
        return LoggerContext.getMethodContext().isTraceChange();

    }

    private void afterInvoke(boolean success,
                             SqlCmdHandler sqlCmdHandler,
                             MybatisMethodInvocation methodInvocation) {
        try {
            if (!success) {
                return;
            }
            sqlCmdHandler.postExecute(methodInvocation);

            if (methodInvocation.getRowChangeData() == null) {
                return;
            }
            if (this.sqlCmdEmitter == null) {
                log.debug(methodInvocation.getRowChangeData().toString());
                return;
            }
            sqlCmdEmitter.onEvent(methodInvocation.getSqlCmd(),
                methodInvocation.getRowChangeData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean beforeInvoke(MybatisMethodInvocation methodInvocation,
                                 SqlCmdHandler sqlCmdHandler) {
        boolean success = true;
        try {
            sqlCmdHandler.preExecute(methodInvocation);
        } catch (Exception e) {
            success = false;
            log.error(e.getMessage(), e);
        }
        return success;
    }
}
