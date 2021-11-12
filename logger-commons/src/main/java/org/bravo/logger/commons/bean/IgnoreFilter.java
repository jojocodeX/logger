package org.bravo.logger.commons.bean;

import java.util.ArrayList;
import java.util.List;

public interface IgnoreFilter {

    IgnoreFilter DEFAULT_INSTANCE = ArrayList::new;

    List<String> ignoreField();
}