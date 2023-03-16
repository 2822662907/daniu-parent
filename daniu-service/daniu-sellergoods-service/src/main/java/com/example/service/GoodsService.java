package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.GoodsEntity;
import com.example.entity.PageResult;
import com.example.pojo.Goods;

import java.util.List;

/****
 * @Author:jeff
 * @Description:Goods业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface GoodsService extends IService<Goods> {

    /***
     * Goods多条件分页查询
     * @param goods
     * @param page
     * @param size
     * @return
     */
    PageResult<Goods> findPage(Goods goods, int page, int size);

    /***
     * Goods分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<Goods> findPage(int page, int size);

    /***
     * Goods多条件搜索方法
     * @param goods
     * @return
     */
    List<Goods> findList(Goods goods);

    /***
     * 删除Goods
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Goods数据
     * @param goods
     */
    void update(Goods goods);

    /***
     * 新增Goods
     * @param goodsEntity
     */
    void add(GoodsEntity goodsEntity);

    /**
     * 根据ID查询Goods
     * @param id
     * @return
     */
     GoodsEntity findById(Long id);

    /***
     * 查询所有Goods
     * @return
     */
    List<Goods> findAll();
    /***
     * 商品审核
     * @param goodsId
     */
    void audit(Long goodsId);

    /**
     * 商品下架
     * @param goodsId
     */
     void pull(Long goodsId);

    /**
     * s商品上架
     * @param goodsId
     */
    void put(Long goodsId);

    /**
     * 根据id批量上传多个
     * @param Ids
     * @return
     */
    int putMany(Long[] Ids);

    /**
     * 商品批量下架
     * @param ids
     */
    int pullMany(Long[] ids);
}
