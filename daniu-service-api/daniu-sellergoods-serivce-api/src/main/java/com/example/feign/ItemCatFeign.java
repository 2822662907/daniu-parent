package com.example.feign;


import com.example.entity.Result;
import com.example.pojo.ItemCat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "DANIU-SELLERGOODS")
@RequestMapping("/itemCat")
public interface ItemCatFeign {

    /**
     * 获取分类的对象信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<ItemCat> findById(@PathVariable Long id);
}
