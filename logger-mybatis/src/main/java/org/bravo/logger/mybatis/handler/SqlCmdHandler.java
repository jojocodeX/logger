package org.bravo.logger.mybatis.handler;

import org.bravo.logger.mybatis.mapped.MybatisMethodInvocation;
/**
 * @author hejianbing
 * @version @Id: InsertSqlCmdHandler.java, v 0.1 2021年11月12日 19:40 hejianbing Exp $
 */
public interface SqlCmdHandler {
	void preExecute(MybatisMethodInvocation methodInvocation) throws Exception;

	default void postExecute(MybatisMethodInvocation methodInvocation) throws Exception{
	}
	
}
