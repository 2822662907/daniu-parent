package com.example.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.content.pojo.Content;
import com.example.entity.PageResult;

import java.util.List;

/****
 * @Author:jeff
 * @Description:Content业务层接口
 * @Date 2021/2/1 14:19
 *****/

public interface ContentService extends IService<Content> {

    /***
     * Content多条件分页查询
     * @param content
     * @param page
     * @param size
     * @return
     */
    PageResult<Content> findPage(Content content, int page, int size);

    /***
     * Content分页查询
     * @param page
     * @param size
     * @return
     */
    PageResult<Content> findPage(int page, int size);

    /***
     * Content多条件搜索方法
     * @param content
     * @return
     */
    List<Content> findList(Content content);

    /***
     * 删除Content
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Content数据
     * @param content
     */
    void update(Content content);

    /***
     * 新增Content
     * @param content
     */
    void add(Content content);

    /**
     * 根据ID查询Content
     * @param id
     * @return
     */
     Content findById(Long id);

    /***
     * 查询所有Content
     * @return
     */
    List<Content> findAll();
    /***
     * 根据categoryId查询广告集合
     * @param id
     * @return
     */
    List<Content> findByCategoryId(Long id);
}
