/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.beancopy;

import org.bravo.logger.commons.bean.IgnoreFilter;

import java.util.Arrays;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: LoggerIgnoreFilter.java, v 0.1 2021年10月27日 14:49 hejianbing Exp $
 */
public class LoggerIgnoreFilter implements IgnoreFilter {

    @Override
    public List<String> ignoreField() {
        return Arrays.asList("methodEvent","bizChangeItems","result");
    }
}