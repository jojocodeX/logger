/** 
 * copyright(c) 2019-2029 mamcharge.com
 */

package org.bravo.logger.commons.model;

/**
 * 实体Id字段标识接口定义
 * @author hejianbing
 * @Id: Identity.java, v 0.1 2021年10月29日 14:08 hejianbing Exp $
 */
public interface Identity<T> {

    T getId();

    void setId(T t);
}
