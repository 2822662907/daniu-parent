package com.example.bean;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author Jeff
 * @date 2023/3/14 11:24
 * A请求头的令牌信息--->B
 */
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 使用RequestContextHolder获取请求参数对象
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        try {
            if (attributes != null) {
                //取出request
                HttpServletRequest request = attributes.getRequest();
                //获取所有头文件信息的key
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        //头文件的key
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        //将令牌数据添加到头文件中
                        template.header(name, values);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
