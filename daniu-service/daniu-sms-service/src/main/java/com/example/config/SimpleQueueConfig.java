package com.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SimpleQueueConfig {
    /**
     * 简单队列
     */
    private String  simpleQueue = "daniu.sms.queue";
    @Bean
     public Queue simpleQueue(){
        return new Queue(simpleQueue);
    }
}
