package com.sxp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxp.dao.ProductMapper;
import com.sxp.entity.Product;
import com.sxp.entity.ProductES;
import com.sxp.entity.ProductSku;
import com.sxp.entity.ProductVo2;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class ApiTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test() throws IOException {

//        CreateIndexRequest request=new CreateIndexRequest("sago");
//        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
//        System.out.println(createIndexResponse.isAcknowledged());

        //查询所有商品信息
        List<ProductVo2> productVo2s = productMapper.selectProductAll();
        for (int i = 0; i < productVo2s.size(); i++) {
            ProductVo2 vo = productVo2s.get(i);
            String productId= vo.getProductId();
            String productName=vo.getProductName();
            Integer soldNum=vo.getSoldNum();
            List<ProductSku> productSkuList = vo.getProductSkuList();
            int n=productSkuList.size();
            String skuImg=n>0?productSkuList.get(0).getSkuImg():"";
            String skuName=n>0?productSkuList.get(0).getSkuName():"";
            Integer sellPrice=n>0?productSkuList.get(0).getSellPrice():0;
            ProductES product=new ProductES(productId,productName,soldNum,skuImg,skuName,sellPrice);

            //将商品信息存入ES中
            IndexRequest indexRequest=new IndexRequest("sago");
            indexRequest.id(productId);
            indexRequest.source(objectMapper.writeValueAsString(product), XContentType.JSON);
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);



        }
    }
}
