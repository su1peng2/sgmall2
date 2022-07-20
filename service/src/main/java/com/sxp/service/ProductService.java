package com.sxp.service;

import Vo.ResultVo;

public interface ProductService {

    /**
     * 根据关键字查询商品
     * @param keyword
     * @param start
     * @param limit
     * @return
     */
    ResultVo selectProductByKeyword(String keyword,int start,int limit);

    /**
     * 根据分类查询商品品牌
     * @param category
     * @return
     */
    ResultVo getBrandByCategory(String category);

    ResultVo getBrandByKeyword(String keyword);

    /**
     * 根据分类id查询商品
     * @param category
     * @param pageNum
     * @param limit
     * @return
     */
    ResultVo getProductsByCategory(String category,int pageNum,int limit);

    /**
     * 查询推荐商品
     * @return
     */
    ResultVo recommendProduct();

    /**
     * 根据商品id查询商品信息
     * @param pid
     * @return
     */
    ResultVo selectProductById(String pid);

    /**
     * 根据商品Id查询商品详细信息
     * @param pid
     * @return
     */
    ResultVo selectProductParam(String pid);
}
