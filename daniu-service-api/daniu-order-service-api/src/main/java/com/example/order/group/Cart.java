package com.example.order.group;

import com.example.order.pojo.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {
    private String sellerId;//商家id
    private String sellerName;//商家名称
    private List<OrderItem> orderItemList;// 购物车明细
}
