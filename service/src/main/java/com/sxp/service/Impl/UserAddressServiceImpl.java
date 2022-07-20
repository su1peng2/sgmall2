package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.sxp.dao.UserAddrMapper;
import com.sxp.entity.UserAddr;
import com.sxp.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 粟小蓬
 */
@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddrMapper userAddrMapper;
    @Override
    public ResultVo selectAddressById(String userId) {
        Example example = new Example(UserAddr.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("status",1);
        List<UserAddr> userAddrs = userAddrMapper.selectByExample(example);
        return new ResultVo(ResResult.OK,"success",userAddrs);
    }
}
