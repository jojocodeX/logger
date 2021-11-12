/** 
 * copyright(c) 2019-2029 mamcharge.com
 */
 
package org.bravo.logger.mybatis.processor;

import lombok.extern.slf4j.Slf4j;
import org.bravo.logger.commons.context.LoggerContext;
import org.bravo.logger.commons.context.MethodInvokeContext;
import org.bravo.logger.commons.enums.SqlCmd;
import org.bravo.logger.commons.model.ChangeContent;
import org.bravo.logger.commons.model.ChangeItem;
import org.bravo.logger.mybatis.model.RowChangeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hejianbing
 * @Id: DefaultSqlCmdEmitter.java, v 0.1 2021年10月29日 14:08 hejianbing Exp $
 *
 */
@Slf4j
public class DefaultSqlCmdEmitter implements SqlCmdEmitter {

    @Override
    public void onEvent(SqlCmd sqlCmd,List<RowChangeData> changeRows) {
        if (log.isDebugEnabled()) {
            log.debug("{}:{}", sqlCmd.getValue(), changeRows);
        }
        if(changeRows == null || changeRows.size() <= 0){
            return;
        }
        List<ChangeContent> changeItems = this.getChangeContent(changeRows, sqlCmd);

        MethodInvokeContext methodContext = LoggerContext.getMethodContext();

        if (null != methodContext) {
            methodContext.addChangeContent(changeItems);
        }
    }

    /**
     * 把变化的数据组装成服务端需要的数据
     * @param changeRows 变化的数据
     * @param sqlCmd 操作类型
     * */
    private List<ChangeContent> getChangeContent(List<RowChangeData> changeRows, SqlCmd sqlCmd){
        List<ChangeContent> changeContentList = new ArrayList<>(changeRows.size());

        for (RowChangeData row : changeRows) {
            ChangeContent changeContent = new ChangeContent();
            String schema = LoggerContext.getRequestScope().getSchema();
            changeContent.setSchemaName(schema);
            changeContent.setTableName(row.getTableName());
            changeContent.setSqlCmd(sqlCmd.getKey());
            changeContent.setSqlCmdName(sqlCmd.getValue());

            Map<String, ChangeItem> changeMap = row.getChangeColumnMap();
            
            List<ChangeItem> changeItem = mapToList(changeMap);

            if (changeItem == null || changeItem.size() <= 0) {
                continue;
            }
            changeContent.setDiff(changeItem);

            changeContentList.add(changeContent);
        }
        return changeContentList;
    }

    private List<ChangeItem> mapToList(Map<String, ChangeItem> changeMap) {
        if (changeMap == null || changeMap.size() <= 0) {
            return null;
        }
        List<ChangeItem> list = new ArrayList<>(changeMap.size());
        for (Map.Entry<String, ChangeItem> entry : changeMap.entrySet()) {
            ChangeItem value = entry.getValue();
            list.add(value);
        }
        return list;
    }


}
