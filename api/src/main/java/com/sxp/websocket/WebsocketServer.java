package com.sxp.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 粟小蓬
 */
@Component
@ServerEndpoint("/webSocket/{orderId}")
public class WebsocketServer {

    private static ConcurrentHashMap<String, Session> hashMap=new ConcurrentHashMap<>();

    /**
     * 前端触发请求连接，执行此方法
     * @param orderId
     * @param session
     */
    @OnOpen
    public void open(@PathParam("orderId") String orderId, Session session){
        System.out.println(orderId);
        hashMap.put(orderId,session);
    }

    /**
     * 页面关闭或websocket主动关闭
     * @param orderId
     */
    @OnClose
    public void close(@PathParam("orderId") String orderId){
        System.out.println(orderId+"---------close---------");
        hashMap.remove(orderId);
    }

    public static void sendMsg(String orderId,String msg){
        try {
            //接收微信支付接口的参数，返回给前端页面
            hashMap.get(orderId).getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
