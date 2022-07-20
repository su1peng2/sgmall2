package com.sxp;

import com.sxp.dao.*;
import com.sxp.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class ApiApplicationTests {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductCommentsMapper productCommentsMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrdersMapper ordersMapper;

    @Test
    public void testProdcuts(){
        List<ProductVo2> productVo2s = productMapper.selectAllByCategory("46", 0, 4);
        for(ProductVo2 vo2:productVo2s){
            System.out.println(vo2);
        }

    }

    @Test
    public void contextLoads() {
        List<CategoryVo> categoryVos = categoryMapper.selectAllCategory();
        for(CategoryVo categoryVo:categoryVos){
            System.out.println(categoryVo);
        }
    }

    @Test
    public void testProductImg(){
        List<ProductVo> productVoList=productMapper.selectProduct();
        for(ProductVo productVo:productVoList){
            System.out.println(productVo);
        }
    }

    @Test
    public void testCategoryToProduct(){
        List<CategoryVo2> categoryVo2s = categoryMapper.selectOneCategory();
        System.out.println(categoryVo2s.get(2));
        for(ProductVo productVo:categoryVo2s.get(2).getProductVoList()){
            System.out.println(productVo);
        }
    }

    @Test
    public void testProductComments(){
//        List<ProductCommentsVo> productCommentsVos = productCommentsMapper.selectProductCommentVo("3");
//        for(ProductCommentsVo vo:productCommentsVos){
//            System.out.println(vo);
//        }
    }

    @Test
    public void testShoppingCart(){
        List<ShoppingCartVo> shoppingCartVos = shoppingCartMapper.selectShoppingCartByUserId("1");
        for (ShoppingCartVo cartVo:shoppingCartVos){
            System.out.println(cartVo);
        }
    }



    @Test
    @Scheduled(cron = "0/5 * * * * ?")
    public void checkTime(){
        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status","1");
        Date date=new Date(System.currentTimeMillis()-30*60*1000);
        criteria.andLessThan("createTime",date);
        List<Orders> orders = ordersMapper.selectByExample(example);
        for(Orders order:orders){
            System.out.println(order.getCreateTime());
        }
    }
}
