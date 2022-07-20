package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.sxp.dao.ProductCommentsMapper;
import com.sxp.entity.ProductComments;
import com.sxp.entity.ProductCommentsVo;
import com.sxp.service.ProductCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;

/**
 * @author 粟小蓬
 */
@Service
public class ProductCommentServiceImpl implements ProductCommentService {

    @Autowired
    private ProductCommentsMapper productCommentsMapper;

    @Override
    public ResultVo selectProductCommentsById(String pid,int pageNum,int limit) {
//        List<ProductCommentsVo> commentsVos=productCommentsMapper.selectProductCommentVo(pid);

        //计算总条数
        Example example = new Example(ProductComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",pid);
        int count = productCommentsMapper.selectCountByExample(example);

        //计算总页数
        int num=count%limit==0?count/limit:count/limit+1;

        //计算起始索引
        int start=(pageNum-1)*limit;

        List<ProductCommentsVo> list=productCommentsMapper.selectProductCommentVo(pid,start,limit);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("num",num);
        hashMap.put("count",count);
        hashMap.put("list",list);
        return new ResultVo(ResResult.OK,"success",hashMap);
    }

    @Override
    public ResultVo getCommentsCountById(String pid) {
        //求总评价数
        Example example = new Example(ProductComments.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",pid);
        int count = productCommentsMapper.selectCountByExample(example);

        //好评数
        Example example1 = new Example(ProductComments.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("productId",pid);
        criteria1.andEqualTo("commType",1);
        int goodCount = productCommentsMapper.selectCountByExample(example1);

        //好评率
        Double goodRate=goodCount/Double.parseDouble(count+"")*100;
        String rate=(goodRate+"").substring(0,(goodRate+"").lastIndexOf(".")+3);
        //中评数
        Example example2 = new Example(ProductComments.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("productId",pid);
        criteria2.andEqualTo("commType",0);
        int midCount = productCommentsMapper.selectCountByExample(example2);

        //好评数
        Example example3 = new Example(ProductComments.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("productId",pid);
        criteria3.andEqualTo("commType",-1);
        int badCount = productCommentsMapper.selectCountByExample(example3);

        HashMap<String,Object> list=new HashMap<>();
        list.put("count",count);
        list.put("goodCount",goodCount);
        list.put("midCount",midCount);
        list.put("badCount",badCount);
        list.put("goodRate",rate);
        ResultVo resultVo = new ResultVo(ResResult.OK, "success", list);
        return resultVo;
    }


}
