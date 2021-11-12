package org.bravo.logger.commons.bean;

@FunctionalInterface
public interface BeanCopyCallback<S, T> {
    T convert(S source, T t);
}