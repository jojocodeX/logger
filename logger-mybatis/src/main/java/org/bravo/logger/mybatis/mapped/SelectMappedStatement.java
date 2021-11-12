/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.mybatis.mapped;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author hejianbing
 * @version @Id: SelectMappedStatement.java, v 0.1 2021年11月12日 09:27 hejianbing Exp $
 */
@Setter
@Getter
public class SelectMappedStatement {

    private BoundSql        boundSql;

    private MappedStatement mappedStatement;
}