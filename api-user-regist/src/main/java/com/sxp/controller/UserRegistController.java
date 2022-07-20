package com.sxp.controller;

import Vo.ResultVo;
import com.sxp.entity.Users;
import com.sxp.service.UserRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserRegistController {

    @Autowired
    private UserRegistService userRegistService;

    @PostMapping("/regist")
    public ResultVo regist(@RequestBody Users users){
        return userRegistService.regist(users.getUsername(), users.getPassword());
    }
}
