package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxp.dao.UsersMapper;
import com.sxp.entity.Users;
import com.sxp.service.UsersService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import utils.MD5Utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Scope("singleton")
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 注册方法
     * @param username
     * @param password
     * @return
     */
    @Transactional
    public ResultVo regiest(String username, String password) {
        synchronized (this){
            Example example = new Example(Users.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("username",username);
            Users users1=usersMapper.selectOneByExample(example);
            String Md5= MD5Utils.md5(password);
            if(users1==null){
                Users users=new Users();
                users.setUsername(username);
                users.setPassword(Md5);
                users.setUserImg("/img/1.png");
                users.setUserModtime(new Date());
                users.setUserRegtime(new Date());
                int i=usersMapper.insert(users);
                if(i>0){
                    return new ResultVo(ResResult.OK,"注册成功",null);
                }else {
                    return new ResultVo(ResResult.No,"注册失败",null);
                }
            }else {
                return new ResultVo(ResResult.No,"账号已存在",null);
            }
        }

    }

    /**
     * 登录方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResultVo login(String username, String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        Users users=usersMapper.selectOneByExample(example);
        if(users==null){
            return new ResultVo(ResResult.No,"账号不存在",null);
        }else {
            String Md5=users.getPassword();
            if(Md5.equals(MD5Utils.md5(password))){
                JwtBuilder builder = Jwts.builder();
                String token = builder.setId(users.getUserId() + "")
                        //设置用户id为token的id
                        .setSubject(username)
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
