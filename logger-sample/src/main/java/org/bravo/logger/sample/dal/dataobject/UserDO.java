/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.bravo.logger.commons.model.Identity;

import java.util.Date;

/**
 * @author hejianbing
 * @version @Id: UserDO.java, v 0.1 2021年10月29日 14:08 hejianbing Exp $
 */
@Setter
@Getter
@TableName("sys_user_test")
public class UserDO implements Identity<Long> {

    @TableId(type = IdType.AUTO)
    private Long    id;

    private String  name;

    private Integer age;

    private Date    updateTime;
}