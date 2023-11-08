package com.xiongbo.civil.relational.rest;

import com.xiongbo.civil.relational.entities.User;
import com.xiongbo.civil.relational.resources.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class WebHandleController {
    @Resource
    private UserService userService;

    @RequestMapping("/user/insertUser")
    private Long insertUser(User user){
        return userService.insertUser(user);
    }
}
