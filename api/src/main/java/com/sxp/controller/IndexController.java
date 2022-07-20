package com.sxp.controller;

import Vo.ResultVo;
import com.sxp.dao.CategoryMapper;
import com.sxp.service.CategoryService;
import com.sxp.service.IndexImgService;
import com.sxp.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 粟小蓬
 */
@RestController
@RequestMapping("/index")
@CrossOrigin
@Api(value = "对商城首页的接口管理",tags = "首页管理")
public class IndexController {

    @Autowired
    private IndexImgService indexImgService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @ApiModelProperty("商城首页轮播图接口")
    @GetMapping("/indexImg")
    public ResultVo selectIndexImg(){
        return indexImgService.indexImgList();
    }

    @ApiOperation("商品分类信息接口")
    @GetMapping("/category")
    public ResultVo listCategory(){
        return categoryService.listCategory();
    }

    @ApiModelProperty("商品新品推荐接口")
    @GetMapping("/recommendProduct")
    public ResultVo listRecommendProduct(){
        return productService.recommendProduct();
    }

    @ApiModelProperty("分类商品推荐接口")
    @GetMapping("/category-recommend")
    public ResultVo listFirstRecommendProduct(){
        return categoryService.listFirstCategory();
    }
}
