package com.sxp.controller;

import Vo.ResResult;
import Vo.ResultVo;
import com.sxp.entity.Users;
import com.sxp.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 粟小蓬
 */
@RestController()
@RequestMapping("/user")
@CrossOrigin
@Api(value = "对用户的接口管理",tags = "用户管理")
public class UserController {
    @Autowired
    private UsersService usersService;

    /**
     * 日志打印创建
     */
    private Logger logger= LoggerFactory.getLogger(UserController.class);


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "String",name = "username",value = "用户注册账号",required = true),
            @ApiImplicitParam(paramType = "String",name = "password",value = "用户注册密码",required = true)
    })
    @ApiModelProperty("用户注册接口")
    @PostMapping("/regist")
    public ResultVo regist(@RequestBody Users user){
        return usersService.regiest(user.getUsername(),user.getPassword());
    }

    @ApiModelProperty("用户登录接口")
    @GetMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "String",name = "username",value = "用户登录账号",required = true),
            @ApiImplicitParam(paramType = "String",name = "password",value = "用户登录密码",required = true)
    })
    public ResultVo login(String username,String password){
        return usersService.login(username,password);
    }

    @ApiModelProperty("用户登录验证接口")
    @GetMapping("/check")
    public ResultVo check(@RequestHeader String token){
        logger.info("登陆验证打印");
        return new ResultVo(ResResult.OK,"success",null);
    }
}
