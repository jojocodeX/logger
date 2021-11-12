/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.service;

import org.bravo.logger.core.request.UserProfile;
import org.bravo.logger.core.service.UserProfileService;
import org.springframework.stereotype.Component;

/**
 * @author hejianbing
 * @version @Id: DefaultUserProfileService.java, v 0.1 2021年10月26日 12:41 hejianbing Exp $
 */
@Component
public class DefaultUserProfileService implements UserProfileService {
    @Override
    public UserProfile getUser() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserNo("9527");
        userProfile.setUserName("张三");

        return userProfile;
    }
}