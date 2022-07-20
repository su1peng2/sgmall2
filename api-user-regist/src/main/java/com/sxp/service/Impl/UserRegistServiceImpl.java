package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.sxp.entity.Users;
import com.sxp.service.UserRegistService;
import com.sxp.service.feign.UserCheckClient;
import com.sxp.service.feign.UserSaveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.MD5Utils;

import java.util.Date;

/**
 * @author 粟小蓬
 */
@Service
public class UserRegistServiceImpl implements UserRegistService {

    @Autowired
    private UserCheckClient userCheckClient;
    @Autowired
    private UserSaveClient userSaveClient;

    @Override
    public ResultVo regist(String name, String pwd) {
        synchronized (this){
            Users users1= null;
            users1=userCheckClient.userCheck(name);
            String Md5= MD5Utils.md5(pwd);
            if(users1==null){
                Users users=new Users();
                users.setUsername(name);
                users.setPassword(Md5);
                users.setUserImg("/img/1.png");
                users.setUserModtime(new Date());
                users.setUserRegtime(new Date());
                int i= userSaveClient.save(users);
                System.out.println(i+"----------------");
                if(i>0){
                    return new ResultVo(ResResult.OK,"注册成功",null);
                }else {
                    return new ResultVo(ResResult.No,"注册失败",null);
                }
            }else if(users1.getPassword()==null){
                return new ResultVo(ResResult.No,"网络问题，请重新注册",null);
            } else {
                return new ResultVo(ResResult.No,"账号已存在",null);
            }
        }
    }
}
