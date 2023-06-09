package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.PageResult;
import com.example.pojo.ItemCat;

import java.util.List;

/****
 * @Author:jeff
 * @Description:ItemCat业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface ItemCatService extends IService<ItemCat> {

    /***
     * ItemCat多条件分页查询
     * @param itemCat
     * @param page
     * @param size
     * @return
     */
    PageResult<ItemCat> findPage(ItemCat itemCat, int page, int size);

    /***
     * ItemCat分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<ItemCat> findPage(int page, int size);

    /***
     * ItemCat多条件搜索方法
     * @param itemCat
     * @return
     */
    List<ItemCat> findList(ItemCat itemCat);

    /***
     * 删除ItemCat
     * @param id
     */
    void delete(Long id);

    /***
     * 修改ItemCat数据
     * @param itemCat
     */
    void update(ItemCat itemCat);

    /***
     * 新增ItemCat
     * @param itemCat
     */
    void add(ItemCat itemCat);

    /**
     * 根据ID查询ItemCat
     * @param id
     * @return
     */
     ItemCat findById(Long id);

    /***
     * 查询所有ItemCat
     * @return
     */
    List<ItemCat> findAll();
    /**
     * 根据父级ID查询分类列表
     * @param parentId
     * @return
     */
     List<ItemCat> findByParentId(Long parentId);
}
