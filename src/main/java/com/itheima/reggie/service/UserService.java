package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.User;


public interface UserService extends IService<User> {
    void sendMsg(String phone, String subject, String context);
}
