package com.sxp.interceptor;

import Vo.ResResult;
import Vo.ResultVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author 粟小蓬
 * 配置拦截器
 */
@Component
public class WebInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //浏览器预检机制，带请求头的请求先发送options请求，返回后再携带数据发送真正的请求！！！
        String method=request.getMethod();
        if(method.equalsIgnoreCase("OPTIONS")){
            return true;
        }

        //接收token，对token进行验证
        String token=request.getHeader("token");
        if (token == null) {
            ResultVo resultVo=new ResultVo(ResResult.No, "请登录", null);
            doResponse(response,resultVo);
        } else {
            final String s = stringRedisTemplate.boundValueOps(token).get();
            if(s!=null){
                stringRedisTemplate.boundValueOps(token).expire(30, TimeUnit.MINUTES);
                return true;
            }else{
                ResultVo resultVo=new ResultVo(ResResult.No, "请登录", null);
                doResponse(response,resultVo);
            }
        }
        return false;
    }

    /**
     * 返回的方法封装，返回登陆状态和登陆提示
     * @param response
     * @param resultVo
     * @throws IOException
     */
    private void doResponse(HttpServletResponse response,ResultVo resultVo) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String str=new ObjectMapper().writeValueAsString(resultVo);
        writer.write(str);
        writer.flush();
        writer.close();
    }
}
