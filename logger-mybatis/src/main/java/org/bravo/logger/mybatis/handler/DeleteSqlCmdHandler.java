package org.bravo.logger.mybatis.handler;

import lombok.extern.slf4j.Slf4j;
import org.bravo.logger.mybatis.mapped.MybatisMethodInvocation;
import org.bravo.logger.mybatis.mapped.SelectMappedStatement;
import org.bravo.logger.mybatis.model.ColumnChangeData;
import org.bravo.logger.mybatis.model.RowChangeData;
import org.bravo.logger.mybatis.mapped.sqlparser.SqlMeta;
import org.bravo.logger.mybatis.util.DataChangeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @author hejianbing
 * @version @Id: DeleteSqlCmdHandler.java, v 0.1 2021年11月12日 09:39 hejianbing Exp $
 */
@Slf4j
public class DeleteSqlCmdHandler implements SqlCmdHandler {

	@Override
	public void preExecute(MybatisMethodInvocation methodInvocation) throws Exception {
		SqlMeta sqlMeta = methodInvocation.getSqlMeta();

		SelectMappedStatement selectMappedStatement = methodInvocation.getSelectMappedStatement(sqlMeta);
		// 获取要删除的数据
		List<Map<String, Object>> beforeResults = methodInvocation.getExecutor().queryForMap(
				selectMappedStatement.getMappedStatement(),
				methodInvocation.getParameterObject(),
				selectMappedStatement.getBoundSql());

		if(beforeResults == null || beforeResults.isEmpty()){
			return ;
		}
		List<RowChangeData> rowChangeDataList = new ArrayList<>();

		for (Map<String, Object> beforeDataMap : beforeResults) {
			List<ColumnChangeData> columnList = DataChangeUtils.mapToColumnList(beforeDataMap);
			RowChangeData rowChangeData = new RowChangeData();
			rowChangeData.setBeforeColumnList(columnList);
			rowChangeData.setTableName(sqlMeta.getTableName());
			rowChangeData.setChangeColumnMap(rowChangeData.beforeColumnToMap());
			rowChangeDataList.add(rowChangeData);
		}
		methodInvocation.setRowChangeData(rowChangeDataList);
	}

}
