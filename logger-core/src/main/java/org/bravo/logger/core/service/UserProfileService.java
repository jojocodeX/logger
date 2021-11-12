/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.core.service;

import org.bravo.logger.core.request.UserProfile;

/**
 * 获取用户信息
 * @author hejianbing
 * @version @Id: UserProfileService.java, v 0.1 2021年09月22日 16:42 hejianbing Exp $
 */
public interface UserProfileService {

    UserProfile getUser();
}