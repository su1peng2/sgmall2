package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxp.dao.ProductImgMapper;
import com.sxp.dao.ProductMapper;
import com.sxp.dao.ProductParamsMapper;
import com.sxp.dao.ProductSkuMapper;
import com.sxp.entity.*;
import com.sxp.service.ProductService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import utils.PageHelper;

import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImgMapper productImgMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductParamsMapper productParamsMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper=new ObjectMapper();

    public ResultVo selectProductByKeyword2(String keyword, int pageNum, int limit) {
        int start=(pageNum-1)*limit;
        List<ProductVo2> productVo2s = productMapper.selectProductByKeyword(keyword, start, limit);
        //查询总条数
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("productName","%"+keyword+"%");

        int count = productMapper.selectCountByExample(example);
        //查询总页数
        int pageCount=count%limit==0?count/limit:count/limit+1;

        PageHelper<ProductVo2> pageHelper = new PageHelper<>(count,pageCount,productVo2s);

        return new ResultVo(ResResult.OK,"success",pageHelper);
    }

    @Override
    public ResultVo selectProductByKeyword(String keyword, int pageNum, int limit) {
        int start=(pageNum-1)*limit;

        SearchRequest searchRequest = new SearchRequest("sago");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //查询条件
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword,"productName","skuName"));
        //分页条件
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(limit);

        //高亮显示
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        HighlightBuilder.Field field=new HighlightBuilder.Field("productName");
        highlightBuilder.field(field);
        highlightBuilder.preTags("<label style='color:red'>");
        highlightBuilder.postTags("</label>");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = search.getHits();
        //显示为想要的格式
        List<ProductES> products=new ArrayList<>();
        Iterator<SearchHit> iterator = hits.iterator();
        while(iterator.hasNext()){
            SearchHit next = iterator.next();
            String sourceAsString = next.getSourceAsString();
            ProductES product= null;
            try {
                product = objectMapper.readValue(sourceAsString, ProductES.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //高亮字段显示
            HighlightField highlightField = next.getHighlightFields().get("productName");
            if(highlightField!=null){
                Text[] fragments = highlightField.fragments();
                String s = Arrays.toString(fragments);
                product.setProductName(s);
            }
            products.add(product);
        }

        int count =(int) hits.getTotalHits().value;
        //查询总页数
        int pageCount=count%limit==0?count/limit:count/limit+1;

        PageHelper<ProductES> pageHelper = new PageHelper<>(count,pageCount,products);

        return new ResultVo(ResResult.OK,"success",pageHelper);
    }
    @Override
    public ResultVo getBrandByCategory(String category) {
        List<String> list = productMapper.selectBrandByCategory(category);
        return new ResultVo(ResResult.OK,"success",list);
    }

    @Override
    public ResultVo getBrandByKeyword(String keyword) {
        List<String> list=productMapper.selectBrandByKeyword(keyword);
        return new ResultVo(ResResult.OK,"success",list);
    }

    @Override
    public ResultVo getProductsByCategory(String category, int pageNum, int limit) {
        int start=(pageNum-1)*limit;
        List<ProductVo2> productVo2s = productMapper.selectAllByCategory(category, start, limit);
        //查询总条数
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId",category);
        int count = productMapper.selectCountByExample(example);
        //查询总页数
        int pageCount=count%limit==0?count/limit:count/limit+1;

        PageHelper<ProductVo2> pageHelper = new PageHelper<>(count,pageCount,productVo2s);

        return new ResultVo(ResResult.OK,"success",pageHelper);
    }

    @Override
    public ResultVo recommendProduct() {
        ResultVo resultVo = new ResultVo(ResResult.OK, "success", productMapper.selectProduct());
        return resultVo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ResultVo selectProductById(String pid) {
        try {

        //从redis中查询是否存在商品缓存
        String productString = (String)stringRedisTemplate.boundHashOps("products").get(pid);
        if(productString!=null){
            //获取商品信息缓存
            Product product= objectMapper.readValue(productString, Product.class);
            //获取商品图片信息缓存
            String imgString= (String) stringRedisTemplate.boundHashOps("productImgs").get(pid);
            JavaType javaType =objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductImg.class);
            List<ProductImg> productImgs=objectMapper.readValue(imgString,javaType);
            //获取商品套餐信息
            String skuString= (String) stringRedisTemplate.boundHashOps("productSkus").get(pid);
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductSku.class);
            List<ProductSku> productSkus=objectMapper.readValue(skuString,javaType1);

            HashMap<String,Object> baseInfo=new HashMap<>(5);
            baseInfo.put("product",product);
            baseInfo.put("productImgs",productImgs);
            baseInfo.put("productSkus",productSkus);
            return new ResultVo(ResResult.OK,"success",baseInfo);
        }


        //查询商品信息
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",pid);
        criteria.andEqualTo("productStatus",1);
        List<Product> products = productMapper.selectByExample(example);

        if(products.size()!=0){

            //将查询信息添加到缓存中
            stringRedisTemplate.boundHashOps("products").put(products.get(0).getProductId(),
                    objectMapper.writeValueAsString(products.get(0)));

            //查询商品图片信息
            Example example1 = new Example(ProductImg.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("itemId", pid);
            List<ProductImg> productImgs = productImgMapper.selectByExample(example1);

            //将商品图片信息添加到redis缓存中
            stringRedisTemplate.boundHashOps("productImgs").put(products.get(0).getProductId(),
                    objectMapper.writeValueAsString(productImgs));
            //查询商品套餐信息
            Example example2 = new Example(ProductSku.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("productId", pid);
            criteria2.andEqualTo("status",1);
            List<ProductSku> productSkus = productSkuMapper.selectByExample(example2);

            //将商品套餐加入redis缓存中
            stringRedisTemplate.boundHashOps("productSkus").put(products.get(0).getProductId(),
                    objectMapper.writeValueAsString(productSkus));

            HashMap<String,Object> baseInfo=new HashMap<>(5);
            baseInfo.put("product",products.get(0));
            baseInfo.put("productImgs",productImgs);
            baseInfo.put("productSkus",productSkus);

            return new ResultVo(ResResult.OK,"success",baseInfo);
        }else {
            return new ResultVo(ResResult.No,"不存在此商品",null);
        }
        }catch (Exception e){
        }
        return null;
    }

    @Override
    public ResultVo selectProductParam(String pid) {
        Example example = new Example(ProductParams.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",pid);
        List<ProductParams> productParams = productParamsMapper.selectByExample(example);
        if(productParams.size()>0){
            return new ResultVo(ResResult.OK,"success",productParams.get(0));
        }else {
            return new ResultVo(ResResult.No,"返回信息失败",null);
        }
    }
}
