package com.sxp.controller;

import Vo.ResultVo;
import com.sxp.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 粟小蓬
 */
@RestController
@CrossOrigin
@RequestMapping("/address")
@Api(value = "为提供用户地址的接口",tags = "地址管理")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @GetMapping("/list")
    @ApiModelProperty("用户地址接口")
    @ApiImplicitParam(paramType = "String", name = "userId", value = "用户ID", required = true)
    public ResultVo listAddressById(String userId,@RequestHeader String token){
        return userAddressService.selectAddressById(userId);
    }
}
