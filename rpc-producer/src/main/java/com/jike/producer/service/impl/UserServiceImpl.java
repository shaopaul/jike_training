package com.jike.producer.service.impl;

import com.jike.producer.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public String findById() {
        return "user{id=1,username=shaopaul}";
    }
}
