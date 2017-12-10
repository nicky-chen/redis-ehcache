package com.nicky.service;

import com.nicky.domain.Users;
import com.nicky.mapper.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Nicky_chin  --Created on 2017/11/13
 */
@Service
public class UserService {

    @Autowired
    private UsersDAO usersDAO;

    @Cacheable("userCache")
    public Users getUserInfoById(Integer id) {
        Users p = usersDAO.selectByPrimaryKey(id);
        System.err.println("为id、key为:" + p.getId() + "数据做了缓存");
        return p;
    }

}
