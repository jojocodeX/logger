/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.mybatis.executor;


import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hejianbing
 * @version @Id: MybatisExecutor.java, v 0.1 2021年11月12日 09:39 hejianbing Exp $
 */
public class MybatisExecutor {

    private final Executor executor;
    private final boolean  mapUnderscoreToCamelCase;

    public MybatisExecutor(Executor executor, boolean mapUnderscoreToCamelCase) {
        this.executor = executor;
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public List<Map<String,Object>> queryForMap(MappedStatement mappedStatement, Object parameter, BoundSql boundSql) throws SQLException {
        Object queryResultList = this.executor.query(mappedStatement, parameter, RowBounds.DEFAULT, null, null, boundSql);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResults = (List<Map<String, Object>>) queryResultList;

        return resultHandle(mapUnderscoreToCamelCase, queryResults);
    }


    private List<Map<String, Object>> resultHandle(boolean mapUnderscoreToCamelCase,
                                                   List<Map<String, Object>> queryResults){
        if (!mapUnderscoreToCamelCase) {
            return queryResults;
        }
        if (queryResults == null) {
            return queryResults;
        }
        ArrayList<Map<String, Object>> ret = new ArrayList<>(queryResults.size());
        for(Map<String, Object> result : queryResults){
            Map<String, Object> newMap = new CaseInsensitiveMap<>();
            for(Map.Entry<String, Object> entry : result.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                String newKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
                newMap.put(newKey, value);
            }
            ret.add(newMap);
        }
        return ret;
    }


}