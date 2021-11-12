/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.store;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.bravo.logger.commons.utils.BeanUtils;
import org.bravo.logger.core.context.LoggerInfo;
import org.bravo.logger.core.dal.dao.LoggerDao;
import org.bravo.logger.core.dal.dataobject.LoggerInfoDO;
import org.bravo.logger.core.beancopy.LoggerIgnoreFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hejianbing
 * @version @Id: JdbcStorageLocator.java, v 0.1 2021年10月25日 21:03 hejianbing Exp $
 */
public class JdbcStorageLocator implements StorageLocator {

    @Autowired
    private LoggerDao loggerDao;

    @Override
    public void locate(LoggerInfo loggerInfo) {
        LoggerInfoDO loggerInfoDO = this.convert2DO(loggerInfo);

        int rows = loggerDao.insert(loggerInfoDO);

        Preconditions.checkArgument(rows>0,"JdbcStorageLocator store system error");
    }

    private LoggerInfoDO convert2DO(LoggerInfo loggerInfo) {
        return BeanUtils.copy(loggerInfo, LoggerInfoDO.class, (src, desc) -> {
            if (src.getResult() != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("result", src.getResult());

                desc.setResult(JSON.toJSONString(result));
            }
            if (CollectionUtils.isNotEmpty(src.getBizChangeItems())) {
                desc.setBizChangeItems(JSON.toJSONString(src.getBizChangeItems()));
            }
            desc.setMethodEvent(src.getEvent());

            return desc;
        }, new LoggerIgnoreFilter());

    }

}