package com.sxp.service.Impl;

import com.sxp.dao.UserMapper;
import com.sxp.entity.Users;
import com.sxp.service.UserSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSaveServiceImpl implements UserSaveService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public int saveUser(Users users) {
        int i = userMapper.insertUseGeneratedKeys(users);
        return i;
    }
}
