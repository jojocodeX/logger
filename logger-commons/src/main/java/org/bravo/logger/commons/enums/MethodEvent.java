/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.commons.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author hejianbing
 * @version @Id: MethodEvent.java, v 0.1 2021年10月27日 09:51 hejianbing Exp $
 */
@Getter
public enum MethodEvent {

    UPDATE("update","更新"),
    INSERT("insert","新增"),
    DELETE("delete","删除"),
    ENABLED("enabled","启用"),
    DISABLED("disabled","禁用");

    private String code;
    private String name;

    MethodEvent(String code, String name) {
        this.code= code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static MethodEvent ofCode(String code) {
        return Arrays.stream(values()).filter(e->e.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseGet(null);
    }

}