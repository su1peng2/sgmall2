package com.sxp.service;

import Vo.ResultVo;
import com.sxp.entity.Orders;

import java.sql.SQLException;

/**
 * @author 粟小蓬
 */
public interface OrderService {

    public void closeOrder(String orderId);

    /**
     * 根据订单Id查询状态
     * @param orderId
     * @return
     */
    ResultVo getStatusById(String orderId);

    /**
     * 根据购物车列表Id获取购物信息
     * 用订单对象传递参数值，生成完整的订单表并保存
     * @param cartIds
     * @param order
     * @return
     * @throws SQLException
     */
    ResultVo getOrderList(String cartIds, Orders order) throws SQLException;

    /**
     * 通过用户Id和订单状态获取订单信息
     * @param userId
     * @param status
     * @param pageNum
     * @param limit
     * @return
     */
    ResultVo getOrdersByUerIdAndStatus(String userId,String status,int pageNum,int limit);
}
