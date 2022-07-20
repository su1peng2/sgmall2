package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxp.dao.IndexImgMapper;
import com.sxp.entity.IndexImg;
import com.sxp.service.IndexImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 粟小蓬
 */
@Service
public class IndexImgServiceImpl implements IndexImgService {

    @Autowired
    private IndexImgMapper indexImgMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public ResultVo indexImgList() {


        List<IndexImg> indexImgs=null;
        try {
        //获取图片信息
        String indexImg=stringRedisTemplate.boundValueOps("indexImg").get();
        if(indexImg!=null){
            //返回缓存信息
            JavaType javaType=objectMapper.getTypeFactory().constructParametricType(ArrayList.class,IndexImg.class);
            indexImgs=objectMapper.readValue(indexImg,javaType);
        }else{
            //当多个并发请求访问时，防止缓存击穿。
            //双重检测锁，多个并发请求访问数据库，加锁，加锁前检测缓存，加锁后检测缓存，使第一个请求访问一次数据库，后面的并发请求都访问redis缓存。
            synchronized (this){
                String indexImg2=stringRedisTemplate.boundValueOps("indexImg").get();
                if(indexImg2!=null){
                    //返回缓存信息
                    JavaType javaType=objectMapper.getTypeFactory().constructParametricType(ArrayList.class,IndexImg.class);
                    indexImgs=objectMapper.readValue(indexImg,javaType);
                }else{
                    //读取数据库信息
                    indexImgs=indexImgMapper.indexImgList();
                    //防止大量请求访问到空的redis-》数据库不存在的信息，增加数据库压力--防止缓存穿透
                    //增加对数据库结果的判断，为空则设置一个临时的数据结构存到redis，以便后来的请求访问redis，设置数据在一定的时间内过期
                    if(indexImgs==null){
                        List<IndexImg> list=new ArrayList<>();
                        String str=objectMapper.writeValueAsString(list);
                        stringRedisTemplate.boundValueOps("indexImg").set(str,10,TimeUnit.SECONDS);
                    }else{
                        //存入redis缓存
                        stringRedisTemplate.boundValueOps("indexImg").set(objectMapper.writeValueAsString(indexImgs));
                        //设置过期时间
                        stringRedisTemplate.boundValueOps("indexImg").expire(1, TimeUnit.DAYS);
                    }

                }
            }

        }

        }catch (Exception e){
        }
        return new ResultVo(ResResult.OK,"success",indexImgs);
    }
}
