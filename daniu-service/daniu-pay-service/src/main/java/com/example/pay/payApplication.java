package com.example.pay;

import com.example.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * @author lidi
 * @create 2023-03-16 11:43
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class payApplication {
    public static void main(String[] args) {
        SpringApplication.run(payApplication.class);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
