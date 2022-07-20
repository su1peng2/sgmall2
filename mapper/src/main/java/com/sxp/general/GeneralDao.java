package com.sxp.general;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * @author 粟小蓬
 */
public interface GeneralDao<T> extends Mapper<T>, MySqlMapper<T> {
}
