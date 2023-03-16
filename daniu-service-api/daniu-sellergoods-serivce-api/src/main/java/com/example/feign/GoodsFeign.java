package com.example.feign;

import com.example.entity.GoodsEntity;
import com.example.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("DANIU-SELLERGOODS")
@RequestMapping("Goods")
public interface GoodsFeign {
    @GetMapping("/{id}")
    public Result<GoodsEntity> findById(@PathVariable Long id);
}
