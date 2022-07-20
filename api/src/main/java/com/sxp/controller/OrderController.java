package com.sxp.controller;

import Vo.ResResult;
import Vo.ResultVo;
import com.github.wxpay.sdk.WXPay;
import com.sxp.config.MyWxPayConfig;
import com.sxp.entity.Orders;
import com.sxp.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 粟小蓬
 */
@RestController
@CrossOrigin
@RequestMapping("/order")
@Api(value = "为订单操作设置的接口",tags = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiModelProperty("订单增加接口")
    @ApiImplicitParam(paramType = "String", name = "cartIds", value = "购物列表ID集合", required = true)
    @PostMapping("/add")
    public ResultVo addOrder(String cartIds, @RequestBody Orders order,@RequestHeader String token){
        ResultVo resultVo=null;
        try {
            resultVo=orderService.getOrderList(cartIds, order);
            return resultVo;
        } catch (SQLException e) {
            return new ResultVo(ResResult.No,"订单添加失败",null);
        }
    }

    @ApiModelProperty("订单状态接口")
    @ApiImplicitParam(name = "orderId", value = "订单Id", required = true)
    @GetMapping("/selectById/{orderId}")
    public ResultVo getOrderById(@PathVariable("orderId") String orderId,@RequestHeader String token){
        System.out.println(orderId);
        return orderService.getStatusById(orderId);
    }

    @ApiModelProperty("订单查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true),
            @ApiImplicitParam(name = "status", value = "订单状态", required = false),
            @ApiImplicitParam(name = "pageNum", value = "查询页数", required = true),
            @ApiImplicitParam(name = "limit", value = "每页数量", required = true)
    })
    @GetMapping("/list")
    public ResultVo listOrders(@RequestHeader String token, String userId,String status,int pageNum,int limit){
        System.out.println(status);
        return orderService.getOrdersByUerIdAndStatus(userId, status, pageNum, limit);
    }
}
