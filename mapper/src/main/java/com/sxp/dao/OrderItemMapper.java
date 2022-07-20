package com.sxp.dao;

import com.sxp.entity.OrderItem;
import com.sxp.general.GeneralDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemMapper extends GeneralDao<OrderItem> {

    /**
     *根据订单id查询商品快照
     * @param orderId
     * @return
     */
    List<OrderItem> selectItemByOrderId(String orderId);
}