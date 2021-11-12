package org.bravo.logger.mybatis.handler;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.bravo.logger.commons.enums.SqlCmd;
import java.util.Map;
/**
 * @author hejianbing
 * @version @Id: SqlCmdHandleManager.java, v 0.1 2021年11月12日 19:43 hejianbing Exp $
 */
public class SqlCmdHandleManager {

	private SqlCmdHandleManager() {
	}

	private static Map<SqlCmd, SqlCmdHandler> cmdHandleMap = new CaseInsensitiveMap<>();

	static {
		cmdHandleMap.put(SqlCmd.UPDATE, new UpdateSqlCmdHandler());
		cmdHandleMap.put(SqlCmd.INSERT, new InsertSqlCmdHandler());
		cmdHandleMap.put(SqlCmd.DELETE, new DeleteSqlCmdHandler());
	}

	public static SqlCmdHandler getSqlCmd(SqlCmd sqlCmd) {
		return cmdHandleMap.get(sqlCmd);
	}
}
