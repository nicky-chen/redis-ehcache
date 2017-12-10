package com.nicky.controller;

import com.nicky.cache.redis.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Nicky_chin  --Created on 2017/11/13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private RedisClient redisClient;

    @Test
    public void findUserInfo() throws Exception {
        for (int i = 0; i < 2; i++) {
            userController.findUserInfo(505);
        }
    }

    @Test public void redisTest() {
        redisClient.set("bbb", "sss");
    }

}
