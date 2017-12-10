package com.nicky.controller;

import com.nicky.domain.Users;
import com.nicky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nicky_chin  --Created on 2017/11/13
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("findUser")
    public Users findUserInfo(@RequestParam  Integer id) {
        Users s = userService.getUserInfoById(id);
        return s;
    }

}
