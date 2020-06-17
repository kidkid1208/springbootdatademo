package com.lzh.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.lzh.Mapper.UserMapper;
import com.lzh.Pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames="user"/*,cacheManager = "employeeCacheManager"*/)
public class UserServiceImp implements UserService{
    @Resource
    private List<CacheManager> cacheManagers;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    CacheManager cacheManager;

    public List<User> queryUserList(){

        return userMapper.queryUserList();
    }

    @Cacheable(value = {"user"},key="#id")
    public User queryUserById(int id){

        log.info("get");
        for (CacheManager c : cacheManagers) {

            System.out.println(c.getCacheNames());
        }
        return userMapper.queryUserById(id);

    }

    @CachePut(/*value = "user",*/key = "#user.id")
    public User addUser(User user){

        log.info("create");
        userMapper.addUser(user);
        return user;
    }

    @CachePut(/*value = "user",*/key = "#user.id")
    public User updateUser(User user){

        log.info("update");
        userMapper.updateUser(user);
        return user;
    }

    @CacheEvict(value="emp")
    public void deleteUser(int id){

        log.info("delete");
        userMapper.deleteUser(id);
    }

    @Override
    public User getUser(String username) {
        return userMapper.getUser(username);
    }
}
