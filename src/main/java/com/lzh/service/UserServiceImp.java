package com.lzh.service;

import com.lzh.Mapper.UserMapper;
import com.lzh.Pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserMapper userMapper;


    public List<User> queryUserList(){
        return userMapper.queryUserList();
    }

    public User queryUserById(int id){
        return userMapper.queryUserById(id);
    }

    public int addUser(User user){

        return userMapper.addUser(user);
    }

    public int updateUser(User user){

        return userMapper.updateUser(user);
    }

    public int deleteUser(int id){

        return userMapper.deleteUser(id);
    }

    @Override
    public User getUser(String username) {
        return userMapper.getUser(username);
    }
}
