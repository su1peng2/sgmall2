package com.sxp.service;

import Vo.ResultVo;
import com.sxp.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {



    /**
     * 支付成功，修改支付状态
     * @param orderId
     * @param status
     */
    int updateStatus(String orderId,String status);

    /**
     * 添加购物车列表
     * @param shoppingCart
     * @return
     */
    ResultVo addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 通过用户id查询购物车信息
     * @param userId
     * @return
     */
    ResultVo listShopCartByUserId(String userId);

    /**
     * 通过购物车列表id修改商品数量
     * @param cartId
     * @param cartNum
     * @return
     */
    ResultVo updateNumById(String cartId,Integer cartNum);

    /**
     * 根据购物列表id查询对应购物信息
     * @param cartIds
     * @return
     */
    ResultVo getShopCartByCartIds(String cartIds);
}
