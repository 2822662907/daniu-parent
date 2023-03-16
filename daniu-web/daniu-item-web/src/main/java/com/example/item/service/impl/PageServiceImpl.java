package com.example.item.service.impl;

import com.alibaba.fastjson.JSON;

import com.example.item.service.PageService;
import com.example.entity.GoodsEntity;
import com.example.entity.Result;
import com.example.feign.GoodsFeign;
import com.example.feign.ItemCatFeign;
import com.example.pojo.Goods;
import com.example.pojo.GoodsDesc;
import com.example.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

import java.io.PrintWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {

    @Autowired(required = false)
    private GoodsFeign goodsFeign;

    @Autowired(required = false)
    private ItemCatFeign itemCatFeign;

    @Autowired
    private TemplateEngine templateEngine;


    //生成静态文件路径
    @Value("${pagepath}")
    private String pagepath;

    /**
     * 构建数据模型
     * @param spuId
     * @return
     */
    private Map<String,Object> buildDataModel(Long spuId){
        //构建数据模型
        Map<String, Object> dataMap = new HashMap<>();

        // 分类、SPU、SKU
        //获取SPU 和SKU列表
        Result<GoodsEntity> result = goodsFeign.findById(spuId);
        GoodsEntity goodsEntity = result.getData();
        // 1.加载SPU数据
        Goods goods = goodsEntity.getGoods();
        // 2.加载商品扩展数据
        GoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
        // 3.加载SKU数据
        List<Item> itemList = goodsEntity.getItemList();

        // 4.封装数据模型MODEL
        dataMap.put("goods", goods); // spu
        dataMap.put("goodsDesc", goodsDesc); // 扩展信息spu
        dataMap.put("specificationList", JSON.parseArray(goodsDesc.getSpecificationItems(),Map.class));
        dataMap.put("imageList",JSON.parseArray(goodsDesc.getItemImages(),Map.class));

        dataMap.put("itemList",itemList); // sku

        // 5.加载分类数据
        dataMap.put("category1",itemCatFeign.findById(new Long(goods.getCategory1Id().intValue())).getData());
        dataMap.put("category2",itemCatFeign.findById(new Long(goods.getCategory2Id().intValue())).getData());
        dataMap.put("category3",itemCatFeign.findById(new Long(goods.getCategory3Id().intValue())).getData());

        return dataMap;
    }

    @Override
    public void createPageHtml(Long spuId) {

        // 1.上下文
        Context context = new Context(); // 容器
        Map<String, Object> dataModel = buildDataModel(spuId); // 获取数据
        context.setVariables(dataModel); // 绑定数据

        // 2.准备文件
        File dir = new File(pagepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, spuId + ".html");

        // 3.生成页面
        try (PrintWriter writer = new PrintWriter(dest, "UTF-8")) {
            // 使用模板引擎生成页面
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
