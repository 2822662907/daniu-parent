package com.example.token;


import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class test {
    private String key = "daniu";
    /****
     *创建Jwt令牌
     */
    @Test
    public void testCreateJwt(){
        JwtBuilder builder= Jwts.builder()
                .setId("123")
                .setSubject("zhangsan")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,key);
        System.out.println( builder.compact());
    }
    /***
     *解析Jwt令牌数据
     */
    @Test
    public void testParseJwt(){
        String compactJWt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjQ1NiIsInN1YiI6IumprOWFqOWKnyIsImlhdCI6MTY3ODM0MjA5OCwiYWRkcmVzcyI6Iua1juWNlyIsImdlbmRlciI6IueUtyJ9.VkYkYtK2wqs-GYwQ3EoreZaqHtUlf2UwvzFnZ0G-QqE";
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(compactJWt)
                .getBody();
        JwsHeader header = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(compactJWt)
                .getHeader();
        System.out.println(claims);
        System.out.println(header);
    }
    /**
     * 设置自定义claims
     */
    @Test
    public void testParseJwtMap(){
         Map claims  = new HashMap<>();
         claims.put("address","济南");
         claims.put("gender","男");
        JwtBuilder builder = Jwts.builder()
                .setId("12456")
                .setSubject("马全功")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key);
        builder.addClaims(claims);
        System.out.println(builder.compact());
    }
    /**
     * 设置超时时间
     */
    @Test
    public void testParseJwtTime(){
        JwtBuilder builder = Jwts.builder()
                .setId("1234")
                .setSubject("马全功")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 200004))
                .signWith(SignatureAlgorithm.HS256, "daniu");
        System.out.println(builder.compact());
    }
}
