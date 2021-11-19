package org.bravo.logger.mybatis.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.bravo.logger.commons.model.Identity;
import org.bravo.logger.commons.model.ChangeItem;
import org.bravo.logger.mybatis.mapped.MybatisMethodInvocation;
import org.bravo.logger.mybatis.pojo.ColumnChangeData;
import org.bravo.logger.mybatis.pojo.RowChangeData;
import org.bravo.logger.mybatis.mapped.sqlparser.SqlMeta;
import org.bravo.logger.mybatis.util.ConvertUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @author hejianbing
 * @version @Id: InsertSqlCmdHandler.java, v 0.1 2021年11月12日 09:40 hejianbing Exp $
 */
@Slf4j
public class InsertSqlCmdHandler implements SqlCmdHandler {

	private static final String ID_NAME = "id";

	@Override
	public void preExecute(MybatisMethodInvocation methodInvocation) throws Exception {
		List<Map<String, Object>> requestParameters = methodInvocation.getRequestParameters();

		SqlMeta sqlMeta = methodInvocation.getSqlMeta();
		List<RowChangeData> rowChangeDataList = new ArrayList<>();

		for(Map<String, Object> insertDataMap : requestParameters){
			List<ColumnChangeData> columnList = ConvertUtils.mapToColumnList(insertDataMap);
			RowChangeData changeData = new RowChangeData();
			changeData.setAfterColumnList(columnList);

			changeData.setTableName(sqlMeta.getTableName());
			changeData.setChangeColumnMap(changeData.afterColumnToMap());
			rowChangeDataList.add(changeData);
		}

		methodInvocation.setRowChangeData(rowChangeDataList);
	}

	/**
	 * 回填自动生成的id信息
	 * */
	@Override
	public void postExecute(MybatisMethodInvocation methodInvocation) throws Exception {
		if(CollectionUtils.isEmpty(methodInvocation.getRowChangeData())){
			return ;
		}
		List<RowChangeData> rowChangeData = methodInvocation.getRowChangeData();

		Object parameter = methodInvocation.getParameterObject();
		if(rowChangeData.size() <= 1){
			RowChangeData changeRow = rowChangeData.get(0);
			Object entityId = changeRow.getPrimaryKey();
            if (entityId == null && parameter instanceof Identity) {
                entityId = ((Identity) parameter).getId();
                changeRow.setPrimaryKey(entityId);
                changeRow.getAfterColumnList().add(new ColumnChangeData(ID_NAME, entityId));
                changeRow.getChangeColumnMap().put(ID_NAME, new ChangeItem(ID_NAME, null, entityId));
            }
			return;
		}
		//insertBatch(List<User> users);
		if(parameter instanceof DefaultSqlSession.StrictMap){
			DefaultSqlSession.StrictMap strictMap = (DefaultSqlSession.StrictMap)parameter;
			List list = (List)strictMap.get("list");
			populateField(list, rowChangeData);
		}

		if(parameter instanceof MapperMethod.ParamMap){
			//insertBatch(@Param("users")List<User> users);
			MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap)parameter;
			Set<Map.Entry> entrySet = paramMap.entrySet();
			for(Map.Entry entry : entrySet){
                String key = (String) entry.getKey();
                Object value = entry.getValue();

                if (key.startsWith("param")) {
					if(value instanceof List){
						List<?> list = (List<?>)value;
						populateField(list, rowChangeData);
					}
				}
			}
		}
	}
	private void populateField(List<?> list, List<RowChangeData> changeRows){
		if(list.size() != changeRows.size()){
			return;
		}
		for(int i=0; i<list.size(); i++){
            Object param = list.get(i);
            if (!(param instanceof Identity)) {
                break;
            }
			Identity<?> identifiable = (Identity<?>)param;
			Object id =	identifiable.getId();
			RowChangeData rowData = changeRows.get(i);
			if(rowData.getPrimaryKey() == null && id != null){
				rowData.setPrimaryKey(id);
				rowData.getAfterColumnList().add(new ColumnChangeData(ID_NAME, id));
				rowData.getChangeColumnMap().put(ID_NAME, new ChangeItem(ID_NAME,null,id));
			}
		}
	}
}
