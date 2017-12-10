package com.nicky.mapper;

import com.nicky.domain.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Nicky_chin  --Created on 2017/11/13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UsersDAOTest {

    @Autowired
    private UsersDAO usersDAO;

    @Test public void insert() throws Exception {
        Users u = new Users();
        u.setBirthday(new Date());
        u.setName("nana");
        u.setTelephone("15868199855");
        usersDAO.insert(u);
    }

}
