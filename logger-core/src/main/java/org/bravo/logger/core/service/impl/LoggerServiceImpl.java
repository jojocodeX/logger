/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.service.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bravo.logger.core.request.PageRequest;
import org.bravo.logger.core.response.PageResult;
import org.bravo.logger.commons.utils.BeanUtils;
import org.bravo.logger.core.dal.example.LoggerInfoExample;
import org.bravo.logger.core.domain.ChangeContentDomain;
import org.bravo.logger.core.domain.LoggerInfoDomain;
import org.bravo.logger.core.repository.LoggerRepository;
import org.bravo.logger.core.beancopy.LoggerIgnoreFilter;
import org.bravo.logger.core.request.LoggerPageRequest;
import org.bravo.logger.core.response.ChangeBizContent;
import org.bravo.logger.core.response.LoggerPageResponse;
import org.bravo.logger.core.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author hejianbing
 * @version @Id: LoggerService.java, v 0.1 2021年10月26日 08:51 hejianbing Exp $
 */
@Component
@Lazy
public class LoggerServiceImpl implements LoggerService {

    @Autowired(required = false)
    private LoggerRepository loggerRepository;

    @Override
    public PageResult<LoggerPageResponse> pageQuery(PageRequest<LoggerPageRequest> pageRequest) {
        this.assertPageQuery(pageRequest);

        PageRequest<LoggerInfoExample> condition = this.getPageCondition(pageRequest);

        PageResult<LoggerInfoDomain> loggerInfoPage =  loggerRepository.pageQuery(condition);

        List<LoggerPageResponse> items = convert2ListVO(loggerInfoPage.getList());

        PageResult<LoggerPageResponse> result = new PageResult<>(pageRequest.getCurrentPage(),pageRequest.getPageSize());
        result.setTotalCount(loggerInfoPage.getTotalCount());
        result.setList(items);

        return result;
    }

    private void assertPageQuery(PageRequest<LoggerPageRequest> pageRequest) {
        Preconditions.checkNotNull(pageRequest, "logger pageRequest is null");
        Preconditions.checkNotNull(pageRequest.getCondition(),
            "logger pageRequest condition is null");

        LoggerPageRequest condition = pageRequest.getCondition();

        Preconditions.checkState(StringUtils.isNotBlank(condition.getAppName()),
            "logger pageRequest condition is null");
    }

    private List<LoggerPageResponse> convert2ListVO(List<LoggerInfoDomain> list) {
        return BeanUtils.copyList(list, LoggerPageResponse.class, (src, desc) -> {

            desc.setOnEvent(src.getMethodEvent().getCode());
            desc.setEventName(src.getMethodEvent().getName());
            desc.setResult(src.getResult());

            if (CollectionUtils.isNotEmpty(src.getBizChangeItems())) {
                List<ChangeContentDomain> bizChangeItems = src.getBizChangeItems();

                List<ChangeBizContent> bizChangeModel = BeanUtils.copyList(bizChangeItems, ChangeBizContent.class);

                desc.setBizChangeItems(bizChangeModel);
            }

            return desc;
        },new LoggerIgnoreFilter());

    }

    private PageRequest<LoggerInfoExample> getPageCondition(PageRequest<LoggerPageRequest> pageRequest) {
        PageRequest<LoggerInfoExample> result = new PageRequest<>();
        result.setPageSize(pageRequest.getPageSize());
        result.setCurrentPage(pageRequest.getCurrentPage());

        LoggerInfoExample loggerInfoExample = new LoggerInfoExample();

        LoggerPageRequest requestCondition = pageRequest.getCondition();

        BeanUtils.copyNotNullProperties(requestCondition,loggerInfoExample,"beginDate","endDate");

        if (null != requestCondition.getBeginDate()) {
            loggerInfoExample.setBeginDate(new Date(requestCondition.getBeginDate()));
        }

        if (null != requestCondition.getEndDate()) {
            loggerInfoExample.setEndDate(new Date(requestCondition.getEndDate()));
        }

        result.setCondition(loggerInfoExample);

        return result;
    }
}