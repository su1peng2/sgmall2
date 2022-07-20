package com.sxp.service;

import Vo.ResultVo;

/**
 * 商品分类
 * @author 粟小蓬
 */
public interface CategoryService {
    /**
     * 查询所有分类
     * @return
     */
    ResultVo listCategory();

    /**
     * 查询所有一级分类
     * 查询当前分类下销量最高的六个商品
     * @return
     */
    ResultVo listFirstCategory();
}
