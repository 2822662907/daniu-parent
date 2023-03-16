package com.example.order.service.impl;

import com.example.order.group.Cart;
import com.example.order.pojo.OrderItem;
import com.example.order.service.CartService;
import com.example.feign.ItemFeign;
import com.example.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    @Autowired(required = false)
    private ItemFeign itemFeign;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //1.根据商品SKU ID查询SKU商品信息
        Item item = itemFeign.findById(itemId).getData();
        if (item==null){
            throw new RuntimeException("商品不存在！！");
        }
        if (!item.getStatus().equals("1"))
            throw new RuntimeException("商品状态无效");
        //2.获取商家ID
        String sellerId = item.getSellerId();
        //3.根据商家ID判断购物车列表中是否存在该商家的购物车
        Cart cart= searchCartBySellerId(cartList,sellerId);
        //4.如果购物车列表中不存在该商家的购物车
        if (cart==null){
            // 新建一个购物车
            cart= new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            OrderItem orderItem = createOrderItem(item,num);
            // 4.3 添加订单详情到购物车对象的订单列表
            List orderItemList = new ArrayList();
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            // 4.4将购物车对象添加到购物车列表
            cartList.add(cart);
        }else {
            //存在购物车
            // 判断购物车明细列表中是否存在该商品
          OrderItem orderItem =  searchOrderItemByItemId(cart.getOrderItemList(),itemId);
            if (orderItem==null) {
                // 5.1. 如果没有，新增购物车明细
                orderItem = new OrderItem();
                cart.getOrderItemList().add(orderItem);
            }else {
                // 有就修改数量，总价
                orderItem.setNum(orderItem.getNum()+num);
                orderItem.setTotalFee(new BigDecimal(orderItem.getNum()*orderItem.getPrice().doubleValue()));
                if (orderItem.getNum()<=0){
                    // 删除购物车明细
                    cart.getOrderItemList().remove(orderItem);
                }
                if (cart.getOrderItemList().size()==0)
                //如果移除后cart的明细数量为0，则将cart移除
                    cartList.remove(cart);

            }
        }
        return cartList;
    }
    /**
     * 从redis中查询购物车
     *
     * @param username
     * @return
     */
    @Override
    public List<Cart> findCartListFromRedis(String username) {
        System.out.println("redis中获取"+username);
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList==null){
            cartList = new ArrayList<>();
        }
        return cartList;
    }
    /**
     * 将购物车保存到redis
     *
     * @param username
     * @param cartList
     */
    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        System.out.println("向redis中存"+username);
        redisTemplate.boundHashOps("cartList").put(username,cartList);


    }
    /**
     * 根据商家ID查询购物车对象
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }
    /**
     * 创建订单明细
     * @param item
     * @param num
     * @return
     */
    private OrderItem createOrderItem(Item item, Integer num) {
        if (num<=0)
            throw new RuntimeException("数量非法！！");
        OrderItem orderItem = new OrderItem();
        orderItem.setGoodsId(item.getGoodsId());// 商品id
        orderItem.setItemId(item.getId());//spu id
        orderItem.setPicPath(item.getImage());// 商品图片
        orderItem.setTitle(item.getTitle());// 商品名字
        orderItem.setSellerId(item.getSellerId());// 商家id
        orderItem.setPrice(item.getPrice()); // 商品价格
        orderItem.setNum(num); // 商品数量
        // 总价
        orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*num));
        return orderItem;
    }

    /**
     * 根据商品明细ID查询
     * @param orderItemList
     * @param itemId
     * @return
     */
    private OrderItem searchOrderItemByItemId(List<OrderItem> orderItemList, Long itemId) {
        for (OrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue()==itemId.longValue()){
                return orderItem;
            }
        }
        return null;
    }
}
