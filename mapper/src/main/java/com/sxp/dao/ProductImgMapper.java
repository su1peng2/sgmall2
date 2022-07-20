package com.sxp.dao;

import com.sxp.entity.ProductImg;
import com.sxp.general.GeneralDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImgMapper extends GeneralDao<ProductImg> {
    List<ProductImg> selectImgById(String id);

}