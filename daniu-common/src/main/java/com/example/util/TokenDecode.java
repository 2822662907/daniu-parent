package com.example.util;


import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class TokenDecode {

    //定义公钥文件名称
    public static final String PUBLIC_KEY="public.key";

    //公钥内容字符串
    public static String publickey="";

    //读取公钥
    private String getPubKey(){
        ClassPathResource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            //创建带缓冲区字符流
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //读取第一行公钥数据
            if((publickey=bufferedReader.readLine())!=null){
                return publickey;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    //解析令牌，读取令牌载荷数据
    public Map<String, String> dcodeToken(String token){
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(getPubKey()));
        //获取令牌载荷
        String claims = jwt.getClaims();
        //把载荷json对象转换为 Map
        return JSON.parseObject(claims,Map.class);
    }

    //获取令牌用户名等信息
    public Map<String, String> getUserInfo(){
        //获取SpringSecurity的上下文对象
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails= (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

        //获取令牌
        String tokenValue = oAuth2AuthenticationDetails.getTokenValue();
        //解析令牌，返回map，包含了用户名信息
        return dcodeToken(tokenValue);
    }
}
