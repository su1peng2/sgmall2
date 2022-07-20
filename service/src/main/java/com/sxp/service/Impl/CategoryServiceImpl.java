package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxp.dao.CategoryMapper;
import com.sxp.entity.CategoryVo;
import com.sxp.entity.CategoryVo2;
import com.sxp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public ResultVo listCategory() {

        List<CategoryVo> categoryVo2s = null;
        try{
            //查询redis的分类信息
            String category = stringRedisTemplate.boundValueOps("category").get();
            //判断
            if(category!=null){
                //读取redis的缓存信息
                JavaType javaType=objectMapper.getTypeFactory().constructParametricType(ArrayList.class,CategoryVo.class);
                categoryVo2s = objectMapper.readValue(category, javaType);
            }else {
                categoryVo2s=categoryMapper.selectAllCategory();
                //将查询的信息存入缓存,并设置过期时间
                stringRedisTemplate.boundValueOps("category").set(objectMapper.writeValueAsString(categoryVo2s),1, TimeUnit.DAYS);
            }
        }catch (Exception e){}
        ResultVo resultVo=new ResultVo(ResResult.OK,"success",categoryVo2s);
        return resultVo;
    }

    @Override
    public ResultVo listFirstCategory() {

        List<CategoryVo2> categoryVos = categoryMapper.selectOneCategory();
        ResultVo resultVo = new ResultVo(ResResult.OK, "success",categoryVos );
        return resultVo;
    }
}
