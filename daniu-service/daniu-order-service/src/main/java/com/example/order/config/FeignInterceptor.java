package com.example.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class FeignInterceptor  implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            //使用RequestContextHolder工具获取request相关变量
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes!=null){
                // 获取request
                HttpServletRequest request = attributes.getRequest();
                // 获取所有头信息的key
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames!=null){
                    while (headerNames.hasMoreElements()) {
                        //头文件的key
                        String name  = headerNames.nextElement();
                        //头文件的value
                        String values = request.getHeader(name);
                        requestTemplate.header(name,values);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
