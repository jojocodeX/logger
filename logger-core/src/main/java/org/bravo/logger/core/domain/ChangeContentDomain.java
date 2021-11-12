/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: ChangeContentDomain.java, v 0.1 2021年10月29日 23:58 hejianbing Exp $
 */
@Setter
@Getter
public class ChangeContentDomain {

    /**sql命令编码，1插入，2更新，3删除*/
    private String sqlCmd;

    /** sql命令名称 */
    private String sqlCmdName;

    /**库名*/
    private String schemaName;

    /**表名*/
    private String tableName;

    private List<ChangeItemDomain> diff = new ArrayList<>();

    @Setter
    @Getter
    private static class ChangeItemDomain {
        /**列名*/
        private String name;

        /**旧值*/
        private Object oldValue;

        /**新值*/
        private Object newValue;
    }
}