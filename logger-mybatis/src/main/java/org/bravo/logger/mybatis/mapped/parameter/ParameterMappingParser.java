/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.mybatis.mapped.parameter;

import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author hejianbing
 * @version @Id: ParameterMappingParser.java, v 0.1 2021年11月11日 23:30 hejianbing Exp $
 */
public class ParameterMappingParser {

    public static final String               FOR_EACH_KEY_PREFIX = "__frch_item_";

    MappedStatement                          mappedStatement;
    BoundSql                                 boundSql;
    Object                                   parameterObject;

    public ParameterMappingParser(MappedStatement mappedStatement, BoundSql boundSql,
                                  Object parameterObject) {
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;
        this.parameterObject = parameterObject;
    }

    public List<Map<String, Object>> parse() {
        Configuration configuration = mappedStatement.getConfiguration();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Map<String, Map<String, Object>> map = new CaseInsensitiveMap<>();
        if (mappedStatement.getStatementType() == StatementType.PREPARED) {
            if (parameterMappings != null) {
                for (int i = 0; i < parameterMappings.size(); i++) {
                    ParameterMapping parameterMapping = parameterMappings.get(i);
                    if (parameterMapping.getMode() != ParameterMode.OUT) {
                        Object value;
                        String propertyName = parameterMapping.getProperty();
                        if (boundSql.hasAdditionalParameter(propertyName)) {
                            value = boundSql.getAdditionalParameter(propertyName);
                        } else if (parameterObject == null) {
                            value = null;
                        } else if (mappedStatement.getConfiguration().getTypeHandlerRegistry()
                            .hasTypeHandler(parameterObject.getClass())) {
                            value = parameterObject;
                        } else {
                            MetaObject metaObject = configuration.newMetaObject(parameterObject);
                            value = metaObject.getValue(propertyName);
                        }
                        String indexedName = "";
                        int idx = getPropertyIndex(propertyName);
                        if (idx < 0) {
                            indexedName = FOR_EACH_KEY_PREFIX + "0";
                        } else {
                            indexedName = getIndexedName(propertyName);
                        }
                        Map<String, Object> m = map.get(indexedName);
                        if (m == null) {
                            m = new CaseInsensitiveMap<>();
                            map.put(indexedName, m);
                        }
                        PropertyTokenizer token = new PropertyTokenizer(propertyName);
                        if (token.hasNext()) {
                            m.put(token.getChildren(), value);
                        } else {
                            m.put(propertyName, value);
                        }
                    }
                }
            }
        }
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys, (key1, key2) -> {
            int idx1 = getPropertyIndex(key1);
            int idx2 = getPropertyIndex(key2);
            return idx1 - idx2;
        });
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (String key : keys) {
            Map<String, Object> value = map.get(key);
            if (value != null && value.size() > 0) {
                list.add(value);
            }
        }
        return list;
    }

    public int getPropertyIndex(String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            return -1;
        }
        if (!propertyName.startsWith(FOR_EACH_KEY_PREFIX)) {
            return -1;
        }
        int dot = propertyName.indexOf(".");
        if (dot > 0) {
            propertyName = propertyName.substring(0, dot);
        }
        String[] arr = propertyName.split("_");
        return Integer.parseInt(arr[arr.length - 1]);
    }

    /**
     * 获取属性带索引的前缀部分。
     * 如果是List<Bean>    propertyName = FOR_EACH_KEY_PREFIX+索引.bean的属性名
     * 如果是List<基本类型> propertyName = FOR_EACH_KEY_PREFIX+索引
     * @param propertyName 属性名
     * @return FOR_EACH_KEY_PREFIX+索引
     */
    public static String getIndexedName(String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            return propertyName;
        }
        if (!propertyName.startsWith(FOR_EACH_KEY_PREFIX)) {
            return propertyName;
        }
        int dot = propertyName.indexOf(".");
        if (dot > 0) {
            return propertyName.substring(0, dot);
        } else {
            return propertyName;
        }
    }

    /**
     * 下划线转驼峰
     *
     * @param oldKey 属性名
     * @return FOR_EACH_KEY_PREFIX+索引
     */
    public static String mapUnderscoreToCamelCase(String oldKey) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, oldKey);
    }

}