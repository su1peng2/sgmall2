package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.sxp.dao.OrdersMapper;
import com.sxp.dao.ShoppingCartMapper;
import com.sxp.entity.Orders;
import com.sxp.entity.ShoppingCart;
import com.sxp.entity.ShoppingCartVo;
import com.sxp.entity.ShoppingCartVo2;
import com.sxp.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrdersMapper ordersMapper;

    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public int updateStatus(String orderId, String status) {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setStatus(status);
        int i = ordersMapper.updateByPrimaryKeySelective(orders);
        return i;
    }

    @Override
    public ResultVo addShoppingCart(ShoppingCart shoppingCart) {
        String date=format.format(new Date());
        shoppingCart.setCartTime(date);
        int insert = shoppingCartMapper.insert(shoppingCart);
        if(insert>0){
            return new ResultVo(ResResult.OK,"success",null);
        }else {
            return new ResultVo(ResResult.No,"fail",null);
        }
    }

    @Override
    public ResultVo listShopCartByUserId(String userId) {
        List<ShoppingCartVo> shoppingCartVos = shoppingCartMapper.selectShoppingCartByUserId(userId);
        return new ResultVo(ResResult.OK,"success",shoppingCartVos);
    }

    @Override
    public ResultVo updateNumById(String cartId, Integer cartNum) {
        int i = shoppingCartMapper.updateNumById(cartId, cartNum);
        if(i>0){
            return new ResultVo(ResResult.OK,"success",null);
        }else {
            return new ResultVo(ResResult.No,"update fail",null);
        }
    }

    @Override
    public ResultVo getShopCartByCartIds(String cartIds) {
        String[] split = cartIds.split(",");
        List<String> carts=new ArrayList<>();
        for(int i=0;i< split.length;i++){
            carts.add(split[i]);
        }

        List<ShoppingCartVo2> shoppingCartVos = shoppingCartMapper.selectShopCartByCartIds(carts);
        return new ResultVo(ResResult.OK,"success",shoppingCartVos);
    }
}
