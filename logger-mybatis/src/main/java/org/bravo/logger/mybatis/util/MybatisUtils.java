package org.bravo.logger.mybatis.util;
import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class MybatisUtils {

	public static final String CUSTOMQUERY = "_CUSTOMQUERY";
	public static final String FOR_EACH_KEY_PREFIX = "__frch_item_";
    private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList<>(0);

    /**
     * 根据MappedStatement构造查询MappedStatement
     * @param ms
     * */

    /**
     * 获取dao原始方法
     *
     * @param mappedStatement MappedStatement
     * @return
     */
    public static Method getMethod(MappedStatement mappedStatement) {
        try {
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1);
            final Method[] method = Class.forName(className).getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName)) {
                    return me;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取属性的索引。
     * 如果是List<Bean>    propertyName = FOR_EACH_KEY_PREFIX+索引.bean的属性名
     * 如果是List<基本类型> propertyName = FOR_EACH_KEY_PREFIX+索引
     *
     * @param propertyName 属性名
     * @return
     */
    public static int getPropertyIndex(String propertyName){
        if(StringUtils.isBlank(propertyName)){
            return -1;
        }
        if(!propertyName.startsWith(FOR_EACH_KEY_PREFIX)){
            return -1;
        }
        int dot = propertyName.indexOf(".");
        if(dot > 0){
            propertyName = propertyName.substring(0, dot);
        }
        String[] arr = propertyName.split("_");
        return Integer.parseInt(arr[arr.length-1]);
    }

    /**
     * 获取属性带索引的前缀部分。
     * 如果是List<Bean>    propertyName = FOR_EACH_KEY_PREFIX+索引.bean的属性名
     * 如果是List<基本类型> propertyName = FOR_EACH_KEY_PREFIX+索引
     * @param propertyName 属性名
     * @return FOR_EACH_KEY_PREFIX+索引
     */
    public static String getIndexedName(String propertyName){
        if(StringUtils.isBlank(propertyName)){
            return propertyName;
        }
        if(!propertyName.startsWith(FOR_EACH_KEY_PREFIX)){
            return propertyName;
        }
        int dot = propertyName.indexOf(".");
        if(dot > 0){
            return propertyName.substring(0, dot);
        }else{
            return propertyName;
        }
    }

    /**
     * 下划线转驼峰
     *
     * @param oldKey 属性名
     * @return FOR_EACH_KEY_PREFIX+索引
     */
    public static String mapUnderscoreToCamelCase(String oldKey){
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, oldKey);
    }

}

