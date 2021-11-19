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

