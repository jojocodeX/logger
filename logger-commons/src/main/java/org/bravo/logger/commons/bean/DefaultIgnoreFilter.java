package org.bravo.logger.commons.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: DefaultIgnoreFilter.java, v 0.1 2021年08月28日 22:53 hejianbing Exp $
 */
public class DefaultIgnoreFilter implements IgnoreFilter {

    private List<String> ignoreFields = new ArrayList<>();

    public DefaultIgnoreFilter(List<String> ignoreFields) {
        if (null != ignoreFields && !ignoreFields.isEmpty()) {
            this.ignoreFields = ignoreFields;
        }
    }

    public DefaultIgnoreFilter(String... ignoreFields) {
        if (null!=ignoreFields) {
            this.ignoreFields = Arrays.asList(ignoreFields);
        }
    }

    @Override
    public List<String> ignoreField(){
        return this.ignoreFields;
    }
}