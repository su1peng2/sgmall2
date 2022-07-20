package com.sxp.dao;

import com.sxp.entity.Orders;
import com.sxp.entity.OrdersVo;
import com.sxp.general.GeneralDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 粟小蓬
 */
@Repository
public interface OrdersMapper extends GeneralDao<Orders> {

    /**
     * 根据用户Id和状态分页查询订单
     * @param userId
     * @param status
     * @param start
     * @param limit
     * @return
     */
    List<OrdersVo> selectOrdersByUserIdAndStatus(String userId,String status,int start,int limit);
}