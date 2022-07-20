package com.sxp.dao;

import com.sxp.entity.ProductComments;
import com.sxp.entity.ProductCommentsVo;
import com.sxp.general.GeneralDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCommentsMapper extends GeneralDao<ProductComments> {

    /**
     * 根据商品id查询评论信息
     * @param pid
     * @return
     */
    List<ProductCommentsVo> selectProductCommentVo(String pid,int start,int pageNum);
}