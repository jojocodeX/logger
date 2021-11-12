package org.bravo.logger.commons.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bravo.logger.commons.bean.BeanCopyCallback;
import org.bravo.logger.commons.bean.DefaultIgnoreFilter;
import org.bravo.logger.commons.bean.IgnoreFilter;
import org.bravo.logger.commons.bean.BeanCopyException;
import org.springframework.beans.BeansException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: BeanUtils.java, v 0.1 2021年10月26日 12:58 hejianbing Exp $
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

	
	public static void copyNotNullProperties(Object source, Object target){
		copyNotNullProperties(source, target,null, IgnoreFilter.DEFAULT_INSTANCE);
	}

	public static void copyNotNullProperties(Object source, Object target, String...ignoreProperties)
			throws BeansException {
		copyNotNullProperties(source, target, null, new DefaultIgnoreFilter(ignoreProperties));
	}

	public static void copyProperties(Object source, Object target, IgnoreFilter ignoreFilter)
			throws BeansException {
		copyNotNullProperties(source, target, null, ignoreFilter);
	}
	
	
	public static void copyNotNullProperties(Object source, Object target, Class<?> editable, IgnoreFilter ignoreFilter){
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
						"] not assignable to Editable class [" + editable.getName() + "]");
			}
			actualEditable = editable;
		}
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		List<String> ignoreList = ignoreFilter.ignoreField();

		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null && !ignoreList.contains(targetPd.getName())) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					Method readMethod = sourcePd.getReadMethod();
					try {
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}

						Object value = readMethod.invoke(source);

						if(value!=null && StringUtils.isNotBlank(ObjectUtils.toString(source, ""))){
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					}
					catch (Throwable ex) {
                        throw new RuntimeException(
                            String.format("Could not copy properties [%s] from source to target [%s]"
									, readMethod.getName(),ex.getMessage()));
					}
				}
			}
		}
	}

	public static <S,T> T copy(S source, Class<?> clazz,IgnoreFilter ignoreFilter){
		return copy( source,clazz, null,ignoreFilter);
	}

	public static <T, S> List<T> copyList(List<S> sources, Class<?> clazz,
										  BeanCopyCallback<S, T> copyCallback, String... ignoreProperties){
		return copyList(sources,clazz,copyCallback,new DefaultIgnoreFilter(ignoreProperties));
	}


	public static <T, S> List<T> copyList(List<S> sources, Class<?> clazz,
										  BeanCopyCallback<S, T> copyCallback, IgnoreFilter ignoreFilter) {
		List<T> result = new ArrayList<>();
		if (CollectionUtils.isEmpty(sources)) {
			return result;
		}
		for (S source : sources) {
			T target = copy(source,clazz, copyCallback, ignoreFilter);
			result.add(target);
		}
		return result;
	}

	public static <T, S> List<T> copyList(List<S> sources, Class<?> clazz,IgnoreFilter ignoreFilter) {
		return copyList(sources,clazz,null,ignoreFilter);
	}

	public static <T, S> List<T> copyList(List<S> sources, Class<?> clazz) {
		return copyList(sources,clazz,null,IgnoreFilter.DEFAULT_INSTANCE);
	}

	public static <T, S> T copy( S source,Class<?> clazz, BeanCopyCallback<S, T> copyCallback, IgnoreFilter ignoreFilter) {
        T target = null;
        try {
            target = (T) clazz.newInstance();
        } catch (Exception ex) {
            throw new BeanCopyException("the copy bean does not declare a no-argument construction");
        }
		copyProperties(source, target, ignoreFilter);
        if (null != copyCallback) {
            target = copyCallback.convert(source, target);
        }
        return target;
	}
	
}
