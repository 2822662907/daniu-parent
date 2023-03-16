package com.example.search.feign;

import com.example.entity.Result;
import com.example.pojo.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
@FeignClient("DANIU-SELLERGOODS")
public interface SkuFeign {

    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("/item/status/{status}")
    Result<List<Item>> findByStatus(@PathVariable String status);
}
