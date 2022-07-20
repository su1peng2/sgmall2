package com.sxp.service;

import Vo.ResultVo;
import org.apache.ibatis.annotations.Param;

public interface ProductCommentService {
   ResultVo selectProductCommentsById(
           @Param("pid") String pid,
           @Param("pageNum") int pageNum,
           @Param("limit") int limit);

   /**
    * 获取商品的评价数量，好评，好评率等
    * @param pid
    * @return
    */
   ResultVo getCommentsCountById(String pid);
}
