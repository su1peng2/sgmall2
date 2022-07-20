package com.sxp.controller;

import com.github.wxpay.sdk.WXPayUtil;
import com.sxp.service.ShoppingCartService;
import com.sxp.websocket.WebsocketServer;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 粟小蓬
 */
@RestController
@RequestMapping("/pay")
@CrossOrigin
@Api(value = "为提供微信支付返回的接口",tags = "支付管理")
public class PayController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @RequestMapping("/callBack")
    public String payTest(HttpServletRequest request) throws Exception {

        //通过流读取微信端传来的数据
        ServletInputStream inputStream = request.getInputStream();
        byte[] bytes=new byte[1024];
        int len=-1;
        StringBuilder builder=new StringBuilder();
        while ((len=inputStream.read(bytes))!=-1){
            builder.append(new String(bytes,0,len));
        }
        String s = builder.toString();
        //将字符串转化成map
        Map<String, String> stringStringMap = WXPayUtil.xmlToMap(s);
        System.out.println(stringStringMap.get("result_code"));
        if(stringStringMap!=null&&stringStringMap.get("result_code").equalsIgnoreCase("success")){
            //支付成功

            //改变支付状态
            int i=shoppingCartService.updateStatus(stringStringMap.get("out_trade_no"),"2");
            String oid=stringStringMap.get("out_trade_no");
            //响应前端，改变页面
            WebsocketServer.sendMsg(oid,"2");

            if(i>0){
                //响应微信
                HashMap<String,String> resMap=new HashMap<>();
                resMap.put("return_code","success");
                resMap.put("return_msg","ok");
                resMap.put("app_id",stringStringMap.get("appid"));
                resMap.put("result_code","success");
                return WXPayUtil.mapToXml(resMap);
            }
        }
        return null;
    }
}
