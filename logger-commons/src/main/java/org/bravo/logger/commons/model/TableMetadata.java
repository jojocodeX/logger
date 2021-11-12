/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.commons.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: TableMetadata.java, v 0.1 2021年11月10日 09:36 hejianbing Exp $
 */
@Setter
@Getter
public class TableMetadata {

    private String       schema;

    private String       primaryKey;

    private String       tableName;

    private List<String> columns = new ArrayList<>();
}