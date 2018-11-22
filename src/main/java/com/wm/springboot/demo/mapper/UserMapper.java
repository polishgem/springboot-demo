package com.wm.springboot.demo.mapper;

import com.wm.springboot.demo.domain.User;


public interface UserMapper {

    User query(long id);

    void save(User user);

    void delete(long id);

    void update(User user);

}
