package com.sxp.controller;

import Vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxp.entity.ShoppingCart;
import com.sxp.entity.Users;
import com.sxp.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author 粟小蓬
 */
@RestController
@RequestMapping("/shopCart")
@CrossOrigin
@Api(value = "为购物车创建的接口，管理购物车",tags = "购物车管理")
public class ShopCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/list_pay")
    @ApiModelProperty("订单物品列表接口")
    @ApiImplicitParam(paramType = "String", name = "cartIds", value = "购物列表ID集合", required = true)
    public ResultVo getCartListByCarts(String cartIds,@RequestHeader String token) {

        return shoppingCartService.getShopCartByCartIds(cartIds);
    }

    @GetMapping("/list")
    @ApiModelProperty("购物车列表接口")
    @ApiImplicitParam(paramType = "String", name = "userId", value = "用户ID", required = true)
    public ResultVo getCartList(String userId,@RequestHeader String token) {
        String s = stringRedisTemplate.boundValueOps(token).get();
        try {
            Users users=objectMapper.readValue(s, Users.class);
            System.out.println(users.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return shoppingCartService.listShopCartByUserId(userId);
    }

    @PostMapping("/addShoppingCart")
    @ApiModelProperty("添加购物车列表接口")
    public ResultVo addShoppingCart(@RequestBody ShoppingCart shoppingCart,@RequestHeader String token){
        return shoppingCartService.addShoppingCart(shoppingCart);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "String", name = "cartId", value = "购物车列表ID", required = true),
            @ApiImplicitParam(paramType = "Integer", name = "cartNum", value = "购物车商品数量", required = true)
    })
    @ApiModelProperty("修改购物车商品数量接口")
    @PutMapping("/updateNum")
    public ResultVo updateNumById(String cartId,Integer cartNum,@RequestHeader String token){
        return shoppingCartService.updateNumById(cartId,cartNum);
    }


}
