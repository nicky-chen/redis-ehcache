package com.nicky.mapper;


import com.nicky.domain.Users;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Nicky_chin  --Created on 2017-11-13
 */
@Repository
public interface UsersDAO {
    int deleteByPrimaryKey(Integer id);

    void insert(Users record);

    void insertSelective(Users record);

    void insertBatch(List<Users> records);

    Users selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
}
