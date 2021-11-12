/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.commons.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author hejianbing
 * @version @Id: ChangeItem.java, v 0.1 2021年10月29日 12:36 hejianbing Exp $
 */
@Setter
@Getter
public class ChangeItem implements Serializable {

    /**列名*/
    private String name;

    /**旧值*/
    private Object oldValue;

    /**新值*/
    private Object newValue;

    public ChangeItem(String name, Object oldValue, Object newValue) {
        this.name = name;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

}