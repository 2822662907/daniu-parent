package com.example.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lidi
 * @create 2023-03-16 11:30
 */
@Configuration
public class AliPayConfig {
    @Value("${alipay.appId}")
    private String appId;

    @Value("${alipay.serverUrl}")
    private String serverUrl;

    @Value("${alipay.privateKey}")
    private String privateKey;

    @Value("${alipay.format}")
    private String format;

    @Value("${alipay.charset}")
    private String charset;

    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;

    @Value("${alipay.signType}")
    private String signType;

    // alipay客户端对象
    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(serverUrl,appId,privateKey,format,charset,alipayPublicKey,signType);
    }
}
