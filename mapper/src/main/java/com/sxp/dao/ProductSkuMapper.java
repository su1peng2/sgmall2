package com.sxp.dao;

import com.sxp.entity.ProductSku;
import com.sxp.general.GeneralDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSkuMapper extends GeneralDao<ProductSku> {

    /**
     * 通过商品id查询最低价套餐
     * @param productId
     * @return
     */
    List<ProductSku> selectLowerByProductId(String productId);
}