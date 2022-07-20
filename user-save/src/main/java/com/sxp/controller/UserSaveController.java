package com.sxp.controller;

import com.sxp.entity.Users;
import com.sxp.service.UserSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserSaveController {
    @Autowired
    private UserSaveService userSaveService;

    @PostMapping("/save")
    public int save(@RequestBody Users users){

        int i = userSaveService.saveUser(users);
        System.out.println(i+"--------");
        return i;
    }
}
