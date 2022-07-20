package com.sxp.dao;

import com.sxp.entity.Users;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
@Repository
public interface UserMapper extends Mapper<Users>, MySqlMapper<Users> {
}
