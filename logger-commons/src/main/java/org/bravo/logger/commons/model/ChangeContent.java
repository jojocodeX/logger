/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.commons.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: ChangeContent.java, v 0.1 2021年10月25日 22:35 hejianbing Exp $
 */
@Setter
@Getter
public class ChangeContent implements Serializable {

    /**sql命令编码，1插入，2更新，3删除*/
    private String           sqlCmd;

    /** sql命令名称 */
    private String           sqlCmdName;

    /**库名*/
    private String           schemaName;

    /**表名*/
    private String           tableName;

    /**变化的数据列表*/
    private List<ChangeItem> diff = new ArrayList<>();

}