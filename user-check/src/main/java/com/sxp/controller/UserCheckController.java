package com.sxp.controller;

import com.sxp.entity.Users;
import com.sxp.service.UserCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserCheckController {

    @Autowired
    private UserCheckService userCheckService;
    @GetMapping("/check")
    public Users userCheck(String name){
        return userCheckService.checkUser(name);
    }
}
