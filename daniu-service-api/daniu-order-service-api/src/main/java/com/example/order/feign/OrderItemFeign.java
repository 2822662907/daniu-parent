package com.example.order.feign;


import com.example.order.pojo.OrderItem;
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
@FeignClient(name="order")
@RequestMapping("/orderItem")
public interface OrderItemFeign {

    /***
     * OrderItem分页条件搜索实现
     * @param orderItem
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    Result<PageResult<OrderItem>> findPage(@RequestBody(required = false) OrderItem orderItem, @PathVariable int page, @PathVariable  int size);

    /***
     * OrderItem分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    Result<PageResult<OrderItem>> findPage(@PathVariable  int page, @PathVariable  int size);

    /***
     * 多条件搜索品牌数据
     * @param orderItem
     * @return
     */
    @PostMapping(value = "/search" )
    Result<List<OrderItem>> findList(@RequestBody(required = false) OrderItem orderItem);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改OrderItem数据
     * @param orderItem
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    Result update(@RequestBody OrderItem orderItem,@PathVariable Long id);

    /***
     * 新增OrderItem数据
     * @param orderItem
     * @return
     */
    @PostMapping
    Result add(@RequestBody OrderItem orderItem);

    /***
     * 根据ID查询OrderItem数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<OrderItem> findById(@PathVariable Long id);

    /***
     * 查询OrderItem全部数据
     * @return
     */
    @GetMapping
    Result<List<OrderItem>> findAll();
}