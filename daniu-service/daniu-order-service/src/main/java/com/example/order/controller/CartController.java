package com.example.order.controller;

import com.example.order.group.Cart;
import com.example.order.service.CartService;
import com.example.entity.Result;
import com.example.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 购物车列表
     *
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(String username){
       return cartService.findCartListFromRedis(username);
    }

    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId,Integer num){
        String username = "admin";
        try {
            List<Cart> cartList = findCartList(username);//获取购物车列表
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            cartService.saveCartListToRedis(username,cartList);
            return new Result(true, StatusCode.OK,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR,"添加失败");
        }
    }
}
