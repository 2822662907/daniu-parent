package com.example.order.service;

import com.example.order.group.Cart;

import java.util.List;

public interface CartService {
    /**
     * 添加商品到购物车
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);

    /**
     * 从redis中查询购物车
     * @param username
     * @return
     */
    List<Cart> findCartListFromRedis(String username);
    /**
     * 将购物车保存到redis
     * @param username
     * @param cartList
     */
    void saveCartListToRedis(String username,List<Cart> cartList);
}
