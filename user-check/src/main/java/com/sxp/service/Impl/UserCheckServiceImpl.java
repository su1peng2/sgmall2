package com.sxp.service.Impl;

import com.sxp.dao.UserCheckMapper;
import com.sxp.entity.Users;
import com.sxp.service.UserCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserCheckServiceImpl implements UserCheckService {

    @Autowired
    private UserCheckMapper userCheckMapper;

    @Override
    public Users checkUser(String name) {

        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",name);
        Users users=userCheckMapper.selectOneByExample(example);
        if(users!=null) {
            return users;
        }else{
            return null;
        }


    }
}
