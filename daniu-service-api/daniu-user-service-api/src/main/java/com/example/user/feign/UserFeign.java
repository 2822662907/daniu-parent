package com.example.user.feign;


import com.example.entity.Result;
import com.example.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jeff
 * @date 2023/3/13 14:28
 */
@FeignClient("USER")
@RequestMapping
public interface UserFeign {
    @GetMapping("/user/load/{username}")
    public Result<User> findByUsername(@PathVariable String username);
}
