package com.example.content;


import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.example.content.dao")
@EnableCanalClient
@EnableFeignClients(basePackages = {"com.example.content.feign"})
public class contentApp {
    public static void main(String[] args) {
        SpringApplication.run(contentApp.class);
    }
}
