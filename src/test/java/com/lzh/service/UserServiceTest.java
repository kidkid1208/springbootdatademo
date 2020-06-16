package com.lzh.service;

import com.lzh.Pojo.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Test
    public void queryUserList() {
        List<User> users = userService.queryUserList();
        System.out.println(users);
    }

    @Test
    public void queryUserById() {
        User user = userService.queryUserById(4);
        Assert.assertEquals(user.getUsername(),"ww");
    }

    @Test
    public void addUser() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void getUser() {
    }
}