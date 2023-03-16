package com.example.gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    // 令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";

    // 登陆页面地址
    private static final String USER_LOGIN_URL = "http://localhost:9100/oauth/login";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取Request、Response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //获取请求的URI
        String path = request.getURI().getPath();
        System.out.println(path);

        //如果是登录、goods等开放的微服务[这里的goods部分开放],则直接放行,这里不做完整演示，完整演示需要设计一套权限系统
//        if (path.startsWith("/user/login")|| path.startsWith("/api/brand/search/")){
//            //放行
//            Mono<Void> filter = chain.filter(exchange);
//            return filter;
//        }
        if (URLFilter.hasAuthorize(path)){
            //放行
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }

        //获取头文件中的令牌信息
        String tokent = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        //如果头文件中没有，则从请求参数中获取
        if (StringUtils.isEmpty(tokent)){
            tokent = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }

        // 从cookie中获取令牌信息
        HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
        if (cookie!=null){
            tokent = cookie.getValue();
        }

        //如果为空，则输出错误代码
        if (StringUtils.isEmpty(tokent)){
//            //设置方法不允许被访问，405错误代码
//            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
//            //  拦截
//            return response.setComplete();
            String encodeURL = null;
            try {
                encodeURL = URLEncoder.encode(request.getURI().toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return needAuthorization(USER_LOGIN_URL+"?FROM=" + encodeURL,exchange);
        }

        try {
            // Claims claims = JwtUtil.parseJWT(tokent);

            //判断token是否有Bearer开头,如果没有，就添加Bearer开头
            if(tokent!=null){
                if(!tokent.substring(0,6).toLowerCase().startsWith("bearer")){
                    //添加Bearer开头
                    tokent="Bearer "+tokent;
                }
            }
            //把token转发到对应微服务
            request.mutate().header(AUTHORIZE_TOKEN,tokent);

        } catch (Exception e) {
//            //解析失败，响应401错误
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
            // 获取从哪里来，请求地址
            String encodeURL = null;
            try {
                encodeURL = URLEncoder.encode(request.getURI().toString(), "utf-8");
            } catch (UnsupportedEncodingException ex) {
                e.printStackTrace();
            }
            return needAuthorization(USER_LOGIN_URL+"?FROM=" + encodeURL,exchange);
        }

        return chain.filter(exchange);
    }

    //设置跳转方法
    private Mono<Void> needAuthorization(String url, ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location",url);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
