package com.example.feign;


import com.example.entity.Result;
import com.example.pojo.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "DANIU-SELLERGOODS")
@RequestMapping("/item")
public interface ItemFeign {
    @GetMapping("/{id}")
    public Result<Item> findById(@PathVariable Long id);
    @PostMapping(value = "/item/decr/count")
    public Result decrCount(@RequestParam(value = "username")String username);
}
