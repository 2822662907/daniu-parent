package com.example.order;

import com.example.order.config.FeignInterceptor;
import com.example.util.IdWorker;
import com.example.util.TokenDecode;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author Jeff
 * @date 2023/3/13 17:38
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.example.sellergoods.feign"})
@MapperScan(basePackages = {"com.example.order.dao"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
    /***
     * 创建拦截器Bean对象
     * @return
     */
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }
    // 解析令牌对象
    @Bean
    public TokenDecode getTokenDecode(){
        return new TokenDecode();
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
