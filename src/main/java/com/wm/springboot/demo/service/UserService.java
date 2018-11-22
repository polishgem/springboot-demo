package com.wm.springboot.demo.service;

import com.wm.springboot.demo.domain.User;

public interface UserService {

    User queryUser(long id);
}
