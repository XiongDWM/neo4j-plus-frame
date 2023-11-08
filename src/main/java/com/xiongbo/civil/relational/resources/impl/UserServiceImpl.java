package com.xiongbo.civil.relational.resources.impl;

import com.xiongbo.civil.relational.entities.User;
import com.xiongbo.civil.relational.repository.UserRepository;
import com.xiongbo.civil.relational.resources.UserService;
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
