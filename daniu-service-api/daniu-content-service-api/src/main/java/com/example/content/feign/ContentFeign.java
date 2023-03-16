package com.example.content.feign;


import com.example.content.pojo.Content;
import com.example.entity.PageResult;
import com.example.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:jeff
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(name="content")
public interface ContentFeign {

    /***
     * Content分页条件搜索实现
     * @param content
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/content/search/{page}/{size}" )
    Result<PageResult<Content>> findPage(@RequestBody(required = false) Content content, @PathVariable int page, @PathVariable int size);

    /***
     * Content分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/content/search/{page}/{size}" )
    Result<PageResult<Content>> findPage(@PathVariable int page, @PathVariable int size);

    /***
     * 多条件搜索品牌数据
     * @param content
     * @return
     */
    @PostMapping(value = "/content/search" )
    Result<List<Content>> findList(@RequestBody(required = false) Content content);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/content/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改Content数据
     * @param content
     * @param id
     * @return
     */
    @PutMapping(value="/content/{id}")
    Result update(@RequestBody Content content, @PathVariable Long id);

    /***
     * 新增Content数据
     * @param content
     * @return
     */
    @PostMapping
    Result add(@RequestBody Content content);

    /***
     * 根据ID查询Content数据
     * @param id
     * @return
     */
    @GetMapping("/content/{id}")
    Result<Content> findById(@PathVariable Long id);

    /***
     * 查询Content全部数据
     * @return
     */
    @GetMapping
    Result<List<Content>> findAll();

    /***
     * 根据分类ID查询所有广告
     */
    @GetMapping(value = "/content/list/category/{id}")
    Result<List<Content>> findByCategory(@PathVariable Long id);

}