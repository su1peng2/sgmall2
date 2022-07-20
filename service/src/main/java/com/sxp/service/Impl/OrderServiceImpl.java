package com.sxp.service.Impl;

import Vo.ResResult;
import Vo.ResultVo;
import com.github.wxpay.sdk.WXPay;
import com.sxp.dao.OrderItemMapper;
import com.sxp.dao.OrdersMapper;
import com.sxp.dao.ProductSkuMapper;
import com.sxp.dao.ShoppingCartMapper;
import com.sxp.entity.*;
import com.sxp.service.OrderService;
import com.sxp.service.config.MyWxPayConfig;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import utils.PageHelper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 粟小蓬
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = Exception.class)
    public void closeOrder(String orderId) {
        synchronized (this){
            //4、确认未支付，修改状态
            // 订单状态 6-已关闭 1-超时未支付
            Orders orders1=new Orders();
            orders1.setOrderId(orderId);
            orders1.setStatus("6");
            orders1.setCloseType(1);
            ordersMapper.updateByPrimaryKeySelective(orders1);
            //订单下的商品快照
            Example example1 = new Example(OrderItem.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orderId",orderId);
            List<OrderItem> orderItems = orderItemMapper.selectByExample(example1);
            //5、修改库存，从商品快照将库存加回套餐表
            for(OrderItem item:orderItems){
                ProductSku sku = productSkuMapper.selectByPrimaryKey(item.getSkuId());
                sku.setStock(sku.getStock()+ item.getBuyCounts());
                productSkuMapper.updateByPrimaryKeySelective(sku);
            }
        }

    }

    @Override
    public ResultVo getStatusById(String orderId) {
        Orders orders = ordersMapper.selectByPrimaryKey(orderId);
        if(orders!=null){
            return new ResultVo(ResResult.OK,"success",orders.getStatus());
        }else {
            return new ResultVo(ResResult.No,"fail",null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    //事务处理
    public ResultVo getOrderList(String cartIds, Orders order) throws SQLException {

        //存储返回信息
        HashMap<String,String> payInfo=null;

        //查询订单商品信息
        String[] split = cartIds.split(",");
        List<String> carts=new ArrayList<>();
        for(int i=0;i< split.length;i++){
            carts.add(split[i]);
        }
        List<ShoppingCartVo2> shoppingCartVos = shoppingCartMapper.selectShopCartByCartIds(carts);

        //加锁
        boolean isLock=true;
        String[] skuIds=new String[shoppingCartVos.size()];
        HashMap<String, RLock> map=new HashMap<>(5);
        for(int i=0;i<shoppingCartVos.size();i++){
            String skuId=shoppingCartVos.get(i).getSkuId();
            boolean b=false;
            RLock lock = redissonClient.getLock(skuId);
            try {
                b=lock.tryLock(10,3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(b){
                skuIds[i]=skuId;
                map.put(skuIds[i],lock );
            }
            isLock=isLock&b;
        }

        //加锁判断
        if(isLock){
            String str="";
            boolean f=true;
            //检查库存
            for (int i = 0; i < shoppingCartVos.size(); i++) {
                if(Integer.parseInt(shoppingCartVos.get(i).getCartNum())>shoppingCartVos.get(i).getStock()){
                    f=false;
                }
                if(i==0) {
                    str+=shoppingCartVos.get(i).getProductName();
                } else {
                    str+=","+shoppingCartVos.get(i).getProductName();
                }
            }
            //订单的商品介绍
            order.setUntitled(str);
            if(f){
                //1 生成订单，并保存
                String orderId= UUID.randomUUID().toString().replace("-","");
                //生成订单id
                order.setOrderId(orderId);
                //生成订单创建时间
                order.setCreateTime(new Date());
                //订单状态,未支付
                order.setStatus("1");
                //添加订单
                int insert = ordersMapper.insert(order);
                if(insert>0){
                    //2 生成商品快照
                    for(ShoppingCartVo2 vo:shoppingCartVos){
                        OrderItem orderItem = new OrderItem();
                        String itemId=System.currentTimeMillis()+""+(new Random().nextInt(8999)+1000);
                        orderItem.setOrderId(orderId);
                        orderItem.setItemId(itemId);
                        orderItem.setProductId(vo.getProductId());
                        orderItem.setProductImg(vo.getProductImg());
                        orderItem.setProductName(vo.getProductName());
                        orderItem.setSkuId(vo.getSkuId());
                        orderItem.setSkuName(vo.getSkuName());
                        orderItem.setProductPrice(new BigDecimal(vo.getSellPrice()));
                        orderItem.setBuyCounts(Integer.parseInt(vo.getCartNum()));
                        orderItem.setTotalAmount(new BigDecimal(vo.getSellPrice()*Integer.parseInt(vo.getCartNum())));
                        orderItem.setBasketDate(new Date());
                        orderItem.setBuyTime(new Date());
                        orderItem.setIsComment(0);
                        //添加商品快照
                        int insert1 = orderItemMapper.insert(orderItem);
                    }
                }
                //3扣减库存
                for(ShoppingCartVo2 vo:shoppingCartVos){
                    String skuId=vo.getSkuId();
                    int newStock=vo.getStock()-Integer.parseInt(vo.getCartNum());

                    //设置productSku对象
                    ProductSku productSku = new ProductSku();
                    productSku.setSkuId(skuId);
                    productSku.setStock(newStock);
                    //利用插件将其修改
                    int i = productSkuMapper.updateByPrimaryKeySelective(productSku);
                }

                //4 删减SKU列表中订单选中的商品
                for(String cartId:carts){
                    shoppingCartMapper.deleteByPrimaryKey(cartId);
                }

                for(int i=0;i<skuIds.length;i++){
                    if(!skuIds[i].equals("")||!skuIds[i].equals(null)){
                        map.get(skuIds[i]).unlock();
                    }
                }

                //对微信申请支付订单的过程
                try {
                    //将用户信息配置类添加进支付类中
                    WXPay wxPay=new WXPay(new MyWxPayConfig());
                    //获取支付链接所需的参数集合
                    HashMap<String,String> data=new HashMap<>();
                    //交易描述
                    data.put("body",str);
                    //支付交易的交易号
                    data.put("out_trade_no",orderId);
                    //交易的币种
                    data.put("fee_type","CNY");
                    //交易金额
                    data.put("total_fee","1");
                    //交易采用的类型
                    data.put("trade_type","NATIVE");
                    //支付完成时的回调方法接口
//                data.put("notify_url","http://sxpeng.free.idcfengye.com/pay/callBack");
                    data.put("notify_url","http://47.113.202.125:8080/pay/callBack");
                    Map<String, String> stringStringMap = wxPay.unifiedOrder(data);
                    String payUrl=stringStringMap.get("code_url");

                    payInfo=new HashMap<>();
                    payInfo.put("orderId",orderId);
                    payInfo.put("orderProduct",str);
                    payInfo.put("payUrl",payUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ResultVo(ResResult.OK,"添加订单成功",payInfo);
            }else {
                for(int i=0;i<skuIds.length;i++){
                    if(!skuIds[i].equals("")||!skuIds[i].equals(null)){
                        map.get(skuIds[i]).unlock();
                    }
                }
                return new ResultVo(ResResult.No,"订单提交失败，库存不够",null);

            }
        }
        else{
            for(int i=0;i<skuIds.length;i++){
                if(!skuIds[i].equals("")||!skuIds[i].equals(null)){
                    map.get(skuIds[i]).unlock();
                }
            }
        }
    return new ResultVo(ResResult.No,"订单提交失败",null);
    }

    @Override
    public ResultVo getOrdersByUerIdAndStatus(String userId, String status, int pageNum, int limit) {
        if(status==""){
            status=null;
        }
        int start=(pageNum-1)*limit;
        List<OrdersVo> ordersVos = ordersMapper.selectOrdersByUserIdAndStatus(userId, status, start, limit);

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        if(status!=null){
            criteria.andEqualTo("status",status);
        }
        //订单总条数
        int count = ordersMapper.selectCountByExample(example);
        //订单页数
        int pageCount=count%limit==0?count/limit:count/limit+1;

        PageHelper<OrdersVo> pageHelper = new PageHelper<>(count, pageCount, ordersVos);
        return new ResultVo(ResResult.OK,"success",pageHelper);
    }
}
