/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.bravo.logger.sample.dal.dataobject.UserDO;

/**
 * @author hejianbing
 * @version @Id: UserMapper.java, v 0.1 2021年10月29日 14:04 hejianbing Exp $
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO > {
}