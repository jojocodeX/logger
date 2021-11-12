/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.repository.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.bravo.logger.core.request.PageRequest;
import org.bravo.logger.core.response.PageResult;
import org.bravo.logger.commons.enums.MethodEvent;
import org.bravo.logger.commons.utils.BeanUtils;
import org.bravo.logger.core.dal.dao.LoggerDao;
import org.bravo.logger.core.dal.dataobject.LoggerInfoDO;
import org.bravo.logger.core.dal.example.LoggerInfoExample;
import org.bravo.logger.core.dal.support.PageExecutor;
import org.bravo.logger.core.domain.ChangeContentDomain;
import org.bravo.logger.core.domain.LoggerInfoDomain;
import org.bravo.logger.core.repository.LoggerRepository;
import org.bravo.logger.core.beancopy.LoggerIgnoreFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hejianbing
 * @version @Id: LoggerRepositoryImpl.java, v 0.1 2021年10月26日 09:18 hejianbing Exp $
 */
@Component
@Lazy
public class LoggerRepositoryImpl implements LoggerRepository {

    @Autowired
    private LoggerDao    loggerDao;

    @Autowired
    private PageExecutor pageExecutor;

    @Override
    public PageResult<LoggerInfoDomain> pageQuery(PageRequest<LoggerInfoExample> pageRequest) {

        PageResult<LoggerInfoDomain> pageResult = new PageResult<>(pageRequest.getCurrentPage(), pageRequest.getPageSize());

        Long totalCount = loggerDao.count(pageRequest.getCondition());

        pageResult.setTotalCount(totalCount);

        if (pageResult.getTotalCount() == 0) {

            return pageResult;
        }

        List<LoggerInfoDO> loggerInfoDoList = pageExecutor.pageByCondition(pageResult.offset(), pageRequest.getPageSize(), pageRequest.getCondition());

        List<LoggerInfoDomain> loggerInfoDomainList = this.convert2DomainList(loggerInfoDoList);

        pageResult.setList(loggerInfoDomainList);

        return pageResult;
    }

    private List<LoggerInfoDomain> convert2DomainList(List<LoggerInfoDO> loggerInfoDoList) {
        return BeanUtils.copyList(loggerInfoDoList, LoggerInfoDomain.class, (src, desc) -> {
            desc.setMethodEvent(MethodEvent.ofCode(src.getMethodEvent()));

            String bizItemsJSON = src.getBizChangeItems();

            if (StringUtils.isNotBlank(bizItemsJSON)) {
                List<ChangeContentDomain> bizChangeItems = JSON.parseArray(bizItemsJSON, ChangeContentDomain.class);
                desc.setBizChangeItems(bizChangeItems);
            }

            if (StringUtils.isNotBlank(src.getResult())) {
                desc.setResult(src.getResult());
            }

            return desc;

        }, new LoggerIgnoreFilter());
    }
}