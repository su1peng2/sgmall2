package com.sxp.dao;

import com.sxp.entity.Product;
import com.sxp.entity.ProductVo;
import com.sxp.entity.ProductVo2;
import com.sxp.general.GeneralDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 粟小蓬
 */
@Repository
public interface ProductMapper extends GeneralDao<Product> {

    /**
     * 查询推荐商品
     * @return
     */
    List<ProductVo> selectProduct();

    /**
     * 查询品类中销量前六的商品
     * @param categoryId
     * @return
     */
    List<ProductVo> selectByCategoryId(String categoryId);

    /**
     * 查询三级品类中所有的商品
     * @param categoryId
     * @param start
     * @param limit
     * @return
     */
    List<ProductVo2> selectAllByCategory(String categoryId,int start,int limit);

    /**
     * 根据关键字查询商品
     * @param keyword
     * @param start
     * @param limit
     * @return
     */
    List<ProductVo2> selectProductByKeyword(@Param("keyword") String keyword, int start, int limit);

    /**
     * 查询所有商品
     * @return
     */
    List<ProductVo2> selectProductAll();

    /**
     * 查询当前品类的商品品牌
     * @param categoryId
     * @return
     */
    List<String> selectBrandByCategory(String categoryId);

    /**
     * 根据关键字查询商品品牌
     * @param keyword
     * @return
     */
    List<String> selectBrandByKeyword(String keyword);
}