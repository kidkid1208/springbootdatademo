package com.lzh.service;

import com.lzh.Pojo.User;

import java.util.List;

public interface UserService {


    List<User> queryUserList();

    User queryUserById(int id);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    User getUser(String username);
}
