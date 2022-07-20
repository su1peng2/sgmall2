package com.sxp.service;

import Vo.ResultVo;
import org.springframework.stereotype.Service;

public interface UsersService {

    ResultVo regiest(String username,String password);

    ResultVo login(String username,String password);

}
