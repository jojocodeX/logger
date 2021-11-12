/**
 * bravo.org
 * Copyright (c) 2018-2019 ALL Rights Reserved
 */
package org.bravo.logger.sample.controller;

import com.alibaba.fastjson.JSON;
import org.bravo.logger.sample.api.Result;
import org.bravo.logger.sample.request.UserUpdateRequest;
import org.bravo.logger.sample.response.UserUpdateResponse;
import org.bravo.logger.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hejianbing
 * @version @Id: UserController.java, v 0.1 2021年10月26日 12:58 hejianbing Exp $
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("update")
    public Result<UserUpdateResponse> updateUser() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setUserName("ZS");
        request.setPassword("123");
        request.setAge(123);

        Integer id = userService.update(456,request);

        UserUpdateResponse response = new UserUpdateResponse();
        response.setUserName("LS");
        response.setPassword("123456");
        response.setAge(55);
        response.setId(id);

        Result<UserUpdateResponse> result = new Result<>();

        result.setData(response);
        result.setSuccess(true);

        return result;

    }

    @RequestMapping("pay")
    public String pay(){
        Map<String, String> data = new HashMap<>();
        data.put("name","zs");

        return "redirect:/user/doPay";
    }


    @RequestMapping("doPay")
    @PostMapping
    public @ResponseBody  String doPay(@RequestParam("params") String params){
        System.out.println(params);
        return "xxx";
    }

}