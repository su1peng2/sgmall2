package com.sxp.service;

import Vo.ResultVo;
import com.sxp.entity.UserAddr;

import java.util.List;

/**
 * @author 粟小蓬
 */
public interface UserAddressService {
    /**
     * 通过用户ID获取用户的地址信息
     * @param userId
     * @return
     */
    ResultVo selectAddressById(String userId);
}
