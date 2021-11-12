/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bravo.logger.commons.context.LoggerContext;
import org.bravo.logger.commons.enums.MethodEvent;
import org.bravo.logger.core.configuration.Logger;
import org.bravo.logger.sample.dal.dao.UserMapper;
import org.bravo.logger.sample.dal.dataobject.UserDO;
import org.bravo.logger.sample.model.UserInfo;
import org.bravo.logger.sample.request.UserAddressUpdateRequest;
import org.bravo.logger.sample.request.UserUpdateRequest;
import org.bravo.logger.sample.service.UserAddressService;
import org.bravo.logger.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author hejianbing
 * @version @Id: UserServiceImpl.java, v 0.1 2021年10月26日 12:33 hejianbing Exp $
 */
@Component
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private UserMapper         userMapper;

    @Transactional
    @Logger(onEvent = MethodEvent.UPDATE, traceChange = true,
            description = "更新用户信息 {#request.age}",
            bizNo = "{#xxx}", beforeExecute = "{#root.target.beforeExecute(#root.args[1])}", afterExecute = "{#root.target.afterExecute()}")
    @Override
    public Integer update(Integer id, UserUpdateRequest request) {
        UserAddressUpdateRequest addressUpdateRequest = new UserAddressUpdateRequest();
        addressUpdateRequest.setZipCode("610000");
        addressUpdateRequest.setId(id);

        LoggerContext.addSpelModel("xxx", "3333");

        LoggerContext.addSpelModel("name", "test1");

        UserDO userDO1 = new UserDO();
        userDO1.setName("java " + UUID.randomUUID());
        userDO1.setUpdateTime(new Date());

        UserDO userDO2 = new UserDO();
        userDO2.setName("java " + UUID.randomUUID());
        userDO2.setUpdateTime(new Date());

        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        ids.add(3L);

        List<UserDO> userList = new ArrayList<>();
        userList.add(userDO1);
        userList.add(userDO2);

        //        this.update(userDO1, new LambdaUpdateWrapper<UserDO>()
        //                .in(UserDO::getId, ids));

        this.removeByIds(ids);

        return id;
    }

    public UserInfo beforeExecute(UserUpdateRequest request) {

        UserInfo userInfo = new UserInfo();
        userInfo.setAge("1111111111111111");
        userInfo.setName("111111111111111111");

        return userInfo;
    }

    public Integer afterExecute() {
        return 66666;
    }
}