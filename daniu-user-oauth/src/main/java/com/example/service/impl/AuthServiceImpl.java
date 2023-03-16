package com.example.service.impl;

import com.example.service.AuthService;
import com.example.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;
    /***
     * 授权认证方法
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        if (authToken==null){
            throw new RuntimeException("申请令牌失败");
        }
        return authToken;
    }

    /****
     * 认证方法
     * @param username:用户登录名字
     * @param password：用户密码
     * @param clientId：配置文件中的客户端ID
     * @param clientSecret：配置文件中的秘钥
     * @return
     */
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("USER-AUTH");
        if (serviceInstance!=null){
          throw new RuntimeException("找不到对应服务器");
        }
        //获取令牌的url
        URI uri = serviceInstance.getUri();
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type","password");
        formData.add("username",username);
        formData.add("password",password);
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization",httpbasic(clientId,clientSecret));
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() !=400 &&response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });
        Map map = null;
        try {
            ResponseEntity<Map>  responseEntity= restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(formData, header), Map.class);
            //获取响应数据
            map = responseEntity.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException();
        }
        if (map == null || map.get("access_token")==null || map.get("refresh_token")==null ||
                map.get("jti")==null) {
            //jti是jwt令牌的唯一标识作为用户身份令牌
            throw new RuntimeException("创建令牌失败！");
        }
        //将响应数据封装成AuthToken对象
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(map.get("access_token").toString());
        authToken.setRefreshToken(map.get("refresh_token").toString());
        authToken.setJti(map.get("jti").toString());
        return authToken;
    }

    /***
     * base64编码
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpbasic(String clientId, String clientSecret){
        // 客户端用户名密码拼接
        String string = clientId+":"+clientSecret;
        // 进行base64编码
        byte[] decode = Base64Utils.decode(string.getBytes());
        return "Basic"+new String(decode);
    }
}
