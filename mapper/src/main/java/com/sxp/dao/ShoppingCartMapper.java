package com.sxp.dao;

import com.sxp.entity.ShoppingCart;
import com.sxp.entity.ShoppingCartVo;
import com.sxp.entity.ShoppingCartVo2;
import com.sxp.general.GeneralDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartMapper extends GeneralDao<ShoppingCart> {

    /**
     * 通过userId查找购物车列表
     * @param userId
     * @return
     */
    List<ShoppingCartVo> selectShoppingCartByUserId(String userId);

    /**
     * 通过id修改商品数量
     * @param cartId
     * @param cartNum
     * @return
     */
    int updateNumById(String cartId,Integer cartNum);


    /**
     * 根据购物列表id查询对应购物信息
     * @param cartIds
     * @return
     */
    List<ShoppingCartVo2> selectShopCartByCartIds(List<String> cartIds);
}