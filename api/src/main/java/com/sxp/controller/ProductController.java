package com.sxp.controller;

import Vo.ResultVo;
import com.sxp.service.ProductCommentService;
import com.sxp.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 粟小蓬
 */
@RestController
@RequestMapping("/product")
@CrossOrigin
@Api(value = "为提供商品的详细信息提供的接口",tags = "商品管理")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCommentService productCommentService;



    @GetMapping("/baseInfo")
    @ApiModelProperty("商品基本信息接口")
    public ResultVo selectProductInfoById(String pid){
        return productService.selectProductById(pid);
    }

    @GetMapping("/detail_info/{pid}")
    @ApiModelProperty("商品详细信息接口")
    public ResultVo selectProductParamById(@PathVariable String pid){
        return productService.selectProductParam(pid);
    }

    @GetMapping("/detail_comments/{pid}")
    @ApiModelProperty("商品评论信息接口")
    public ResultVo selectCommentsById(@PathVariable String pid,int pageNum,int limit){
        return productCommentService.selectProductCommentsById(pid,pageNum,limit);
    }

    @GetMapping("/detail_product/{cid}")
    @ApiModelProperty("商品分页接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "查询页数", required = true),
            @ApiImplicitParam(name = "limit", value = "每页数量", required = true)
    })
    public ResultVo selectProductsByCategory(@PathVariable("cid") String category,int pageNum,int limit){
        return productService.getProductsByCategory(category, pageNum, limit);
    }

    @GetMapping("/search_product/")
    @ApiModelProperty("商品搜索分页接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", required = true),
            @ApiImplicitParam(name = "pageNum", value = "查询页数", required = true),
            @ApiImplicitParam(name = "limit", value = "每页数量", required = true)
    })
    public ResultVo selectProductsByKeyword(String keyword,int pageNum,int limit){
        return productService.selectProductByKeyword(keyword, pageNum, limit);
    }

    @GetMapping("/detail_brand/{cid}")
    @ApiModelProperty("商品品牌接口")
    public ResultVo selectBrandByCategory(@PathVariable("cid") String category){
        return productService.getBrandByCategory(category);
    }
    @ApiImplicitParam(name = "keyword", value = "关键字", required = true)
    @GetMapping("/search_brand/")
    @ApiModelProperty("搜索商品品牌接口")
    public ResultVo selectBrandByKeyword(String keyword){
        return productService.getBrandByKeyword(keyword);
    }

    @GetMapping("/detail_commentsCount/{pid}")
    @ApiModelProperty("商品评论信息数量接口")
    public ResultVo selectCommentsCountById(@PathVariable String pid){
        return productCommentService.getCommentsCountById(pid);
    }
}
