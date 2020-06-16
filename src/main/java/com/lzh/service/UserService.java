package com.lzh.service;

import com.lzh.Pojo.User;

import java.util.List;

public interface UserService {


    List<User> queryUserList();

    User queryUserById(int id);

    int addUser(User user);

    int updateUser(User user);

    int deleteUser(int id);

    User getUser(String username);
}
