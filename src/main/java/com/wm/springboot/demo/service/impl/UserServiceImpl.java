package com.wm.springboot.demo.service.impl;

import com.wm.springboot.demo.domain.User;
import com.wm.springboot.demo.mapper.UserMapper;
import com.wm.springboot.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User queryUser(long id) {
        return userMapper.query(id);
    }
}
