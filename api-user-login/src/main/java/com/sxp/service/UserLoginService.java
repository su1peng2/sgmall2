package com.sxp.service;

import Vo.ResultVo;

public interface UserLoginService {
    ResultVo login(String username, String password);
}
