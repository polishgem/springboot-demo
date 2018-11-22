package com.wm.springboot.demo.service;

import com.wm.springboot.demo.domain.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional(propagation = Propagation.REQUIRED)
    User queryUser(long id);
}
