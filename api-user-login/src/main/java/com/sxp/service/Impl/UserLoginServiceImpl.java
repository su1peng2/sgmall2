package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import com.sxp.entity.Users;
import com.sxp.service.UserLoginService;
import com.sxp.service.feign.UserLoginServiceClient;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import utils.MD5Utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 粟小蓬
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserLoginServiceClient userLoginServiceClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public ResultVo login(String name, String password) {
        Users users= userLoginServiceClient.userCheck(name);
        if(users==null){
            return new ResultVo(ResResult.No,"账号不存在",null);
        }else {
            String Md5=users.getPassword();
            if(Md5.equals(MD5Utils.md5(password))){
                JwtBuilder builder = Jwts.builder();
                String token = builder.setId(users.getUserId() + "")
                        //设置用户id为token的id
                        .setSubject(name)
                        //设置主题，token携带的参数
                        .setIssuedAt(new Date())
                        //设置token生成时间
                        .setClaims(null)
                        //存放用户的权限信息map类型
                        .setExpiration(new Date(System.currentTimeMillis() + 24*60*60 * 1000))
                        //设置token的过期时间
                        .signWith(SignatureAlgorithm.HS512, "suxiaopeng")
                        //设置加密方式和加密密码,注意使用HS
                        .compact();

                try {
                    String s = objectMapper.writeValueAsString(users);
                    stringRedisTemplate.boundValueOps(token).set(s,30, TimeUnit.MINUTES);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                ResultVo resultVo=new ResultVo(ResResult.OK,token,null);
                resultVo.setData(users);
                return resultVo;
            }else {
                return new ResultVo(ResResult.No,"登录失败,密码错误",null);
            }
        }
    }
}
