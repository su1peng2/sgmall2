package com.sxp.dao;

import com.sxp.entity.IndexImg;
import com.sxp.general.GeneralDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 粟小蓬
 */
@Repository
public interface IndexImgMapper extends GeneralDao<IndexImg> {


    /**
     ** 查询所有的轮播图信息
     ** 按顺序存在list中
     * @return
     */
    List<IndexImg> indexImgList();
}