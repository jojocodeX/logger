package org.bravo.logger.mybatis.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.bravo.logger.commons.model.ChangeItem;

import java.util.List;
import java.util.Map;

/**
 * @author hejianbing
 * @Id: RowChangeData.java, v 0.1 2021年10月29日 14:08 hejianbing Exp $
 */
@Setter
@Getter
@ToString
public class RowChangeData {

    /** 数据库表名 */
    public String                  tableName;

    /** 对应的数据库表的主键ID*/
    public Object                  primaryKey;

    /** 更改的所有属性的以及它对应的原始值,即更改之前的值*/
    public List<ColumnChangeData> beforeColumnList;

    /**更改的所有属性和值*/
    public List<ColumnChangeData> afterColumnList;

    /** 发生变化的对象的属性及值的键值对*/
    public Map<String, ChangeItem> changeColumnMap;

    public Map<String, ChangeItem> beforeColumnToMap() {
        List<ColumnChangeData> beforeList = this.beforeColumnList;
        if (beforeList == null || beforeList.size() <= 0) {
            return null;
        }
        Map<String, ChangeItem> map = new CaseInsensitiveMap<>();
        for (ColumnChangeData before : beforeList) {
            String name = before.getName();
            Object value = before.getValue();
            map.put(name, new ChangeItem(name, value, null));
        }
        return map;
    }

    public Map<String, ChangeItem> afterColumnToMap() {
        List<ColumnChangeData> afterList = this.afterColumnList;
        if (afterList == null || afterList.size() <= 0) {
            return null;
        }
        Map<String, ChangeItem> map = new CaseInsensitiveMap<>();
        for (ColumnChangeData after : afterList) {
            String name = after.getName();
            Object value = after.getValue();
            map.put(name, new ChangeItem(name, null, value));
        }
        return map;
    }
}
