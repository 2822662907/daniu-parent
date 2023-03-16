package com.example.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "SEARCH")
@RequestMapping("/search")
public interface SearchSkuFeign {

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @PostMapping
    public Map search(@RequestBody(required = false) Map searchMap);
}
