/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.mybatis.util;

import org.bravo.logger.mybatis.pojo.ColumnChangeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hejianbing
 * @version @Id: ConvertUtils.java, v 0.1 2021年11月12日 10:18 hejianbing Exp $
 */
public class ConvertUtils {

    public static List<ColumnChangeData> mapToColumnList(Map<String, Object> dataMap){
        List<ColumnChangeData> columnList = new ArrayList<>();
        if(dataMap == null || dataMap.size() <= 0){
            return columnList;
        }
        for (Map.Entry<String, Object> dataEntry : dataMap.entrySet()) {
            ColumnChangeData changeColumn = new ColumnChangeData();
            changeColumn.setName(dataEntry.getKey());
            changeColumn.setValue(dataEntry.getValue());
            columnList.add(changeColumn);
        }
        return columnList;
    }

}