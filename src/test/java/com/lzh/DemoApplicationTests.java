package com.lzh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.wiring.BeanConfigurerSupport;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Resource
    private List<CacheManager> cacheManagers;
    @Autowired
    DataSource dataSource;
    @Test
    public void contextLoads() {

        System.out.println(dataSource.getClass());
        System.out.println("CacheManager大小为=========" + cacheManagers.size());

        System.out.println("=================================================");

        System.out.println(cacheManagers.toString());
        for (CacheManager c : cacheManagers) {

            System.out.println(c.getCacheNames());
        }
    }



}
