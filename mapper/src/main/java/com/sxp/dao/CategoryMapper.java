package com.sxp.dao;

import com.sxp.entity.Category;
import com.sxp.entity.CategoryVo;
import com.sxp.entity.CategoryVo2;
import com.sxp.general.GeneralDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends GeneralDao<Category> {

    /**
     * 查询所有的商品类别
     * @return
     */
    List<CategoryVo> selectAllCategory();

    /**
     * 查询一级商品及其销量前六的商品
     * @return
     */
    List<CategoryVo2> selectOneCategory();
}