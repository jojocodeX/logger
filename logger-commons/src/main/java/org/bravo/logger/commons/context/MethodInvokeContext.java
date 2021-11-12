/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.commons.context;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.bravo.logger.commons.model.ChangeContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hejianbing
 * @version @Id: MethodInvokeContext.java, v 0.1 2021年10月27日 15:21 hejianbing Exp $
 */
@Setter
@Getter
public class MethodInvokeContext {

    /** 是否追踪变更记录 */
    private boolean             traceChange;

    private Map<String, Object> variable       = new HashMap<>();

    private List<ChangeContent> bizChangeItems = new ArrayList<>();

    public void addChangeContent(ChangeContent bizChangeItem) {
        this.bizChangeItems.add(bizChangeItem);
    }

    public void addChangeContent(List<ChangeContent> bizChangeItems) {
        if (CollectionUtils.isNotEmpty(bizChangeItems)) {
            this.bizChangeItems.addAll(bizChangeItems);
        }
       
    }
}