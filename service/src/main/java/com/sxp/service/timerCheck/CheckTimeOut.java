package com.sxp.service.timerCheck;

import com.github.wxpay.sdk.WXPay;
import com.sxp.dao.OrderItemMapper;
import com.sxp.dao.OrdersMapper;
import com.sxp.dao.ProductSkuMapper;
import com.sxp.entity.OrderItem;
import com.sxp.entity.Orders;
import com.sxp.entity.ProductSku;
import com.sxp.service.OrderService;
import com.sxp.service.config.MyWxPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 粟小蓬
 */
@Component
public class CheckTimeOut {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderService orderService;

    private WXPay wxPay=new WXPay(new MyWxPayConfig());

    @Scheduled(cron = "0/60 * * * * ?")
    public void checkTime() throws Exception {
        System.out.println("-----------------"+new Date());
        //1、获取超时订单
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status","1");
        Date date=new Date(System.currentTimeMillis()-30*60*1000);
        criteria.andLessThan("createTime",date);
        List<Orders> orders = ordersMapper.selectByExample(example);
        //2、向微信平台发送请求确认是否支付
        for(Orders order:orders){
            HashMap<String,String> map=new HashMap<>();
            map.put("out_trade_no",order.getOrderId());
            Map<String, String> payMap = wxPay.orderQuery(map);
            if(payMap.get("trade_state").equalsIgnoreCase("success")){
                //2.1订单实际已支付,修改状态为2
                order.setStatus("2");
                int i = ordersMapper.updateByPrimaryKeySelective(order);
            }else if(payMap.get("trade_state").equalsIgnoreCase("NOTPAY")){
                //2.2订单未支付
                //3、关闭此订单微信支付
                Map<String, String> closeOrder = wxPay.closeOrder(map);
                System.out.println(closeOrder);

                //修改数据库状态
                orderService.closeOrder(order.getOrderId());
            }


        }

    }
}
