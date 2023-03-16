package com.example.entity;

import com.example.pojo.Goods;
import com.example.pojo.GoodsDesc;
import com.example.pojo.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsEntity implements Serializable {
    private Goods goods;//商品信息 SPU
    private GoodsDesc goodsDesc; //商品扩展信息
    private List<Item> itemList;//商品详情 SKU,有多条
}
