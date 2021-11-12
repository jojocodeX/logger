/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: BizChangeModel.java, v 0.1 2021年10月26日 08:57 hejianbing Exp $
 */
@Setter
@Getter
public class ChangeBizContent implements Serializable {

    /**sql命令编码，1插入，2更新，3删除*/
    private String              sqlCmd;

    /** sql命令名称 */
    private String              sqlCmdName;

    /**库名*/
    private String              schemaName;

    /**表名*/
    private String              tableName;

    /**变化的数据列表*/
    private List<ChangeContentItem> diff = new ArrayList<>();

    @Setter
    @Getter
    private static class ChangeContentItem {

        /**列名*/
        private String name;

        /**旧值*/
        private Object oldValue;

        /**新值*/
        private Object newValue;
    }
}