package com.xiongdwm.framework.relational.resources.impl;

import com.xiongdwm.framework.relational.entities.User;
import com.xiongdwm.framework.relational.repository.UserRepository;
import com.xiongdwm.framework.relational.resources.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{
    @Resource
    private UserRepository userRepository;


    @Override
    public Long insertUser(User user) {
        return userRepository.save(user).getId();
    }


}
