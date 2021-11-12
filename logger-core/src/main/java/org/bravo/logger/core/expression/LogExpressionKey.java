/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.expression;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * @author hejianbing
 * @version @Id: LogExpressionKey.java, v 0.1 2021年09月23日 14:14 hejianbing Exp $
 */
public class LogExpressionKey implements Comparable<LogExpressionKey> {

    private final AnnotatedElementKey element;

    private final String              expression;

    public LogExpressionKey(AnnotatedElementKey element, String expression) {
        Assert.notNull(element, "AnnotatedElementKey must not be null");
        Assert.notNull(expression, "Expression must not be null");
        this.element = element;
        this.expression = expression;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LogExpressionKey)) {
            return false;
        }
        LogExpressionKey otherKey = (LogExpressionKey) other;
        return (this.element.equals(otherKey.element)
                && ObjectUtils.nullSafeEquals(this.expression, otherKey.expression));
    }

    @Override
    public int hashCode() {
        return this.element.hashCode() * 29 + this.expression.hashCode();
    }

    @Override
    public String toString() {
        return this.element + " with expression \"" + this.expression + "\"";
    }

    @Override
    public int compareTo(LogExpressionKey other) {
        int result = this.element.toString().compareTo(other.element.toString());
        if (result == 0) {
            result = this.expression.compareTo(other.expression);
        }
        return result;
    }

}