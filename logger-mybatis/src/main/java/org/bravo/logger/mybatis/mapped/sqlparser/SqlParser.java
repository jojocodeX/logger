/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.mybatis.mapped.sqlparser;

import org.apache.ibatis.mapping.MappedStatement;

import java.util.List;

/**
 * @author hejianbing
 * @version @Id: SqlParser.java, v 0.1 2021年11月11日 22:48 hejianbing Exp $
 */
public interface SqlParser {

    List<SqlMeta> getSqlMeta(MappedStatement mappedStatement, Object parameter) throws Exception;

}