package org.bravo.logger.mybatis.processor;

import org.bravo.logger.commons.enums.SqlCmd;
import org.bravo.logger.mybatis.model.RowChangeData;

import java.util.List;
/**
 * @author hejianbing
 * @Id: SqlCmdEmitter.java, v 0.1 2021年10月29日 14:08 hejianbing Exp $
 *
 */
public interface SqlCmdEmitter {

	/**插入数据回调
	 * @param sqlCmd 执行sql命令
	 * @param changeRows 变化数据
	 * */
	void onEvent(SqlCmd sqlCmd, List<RowChangeData> changeRows) ;
}
