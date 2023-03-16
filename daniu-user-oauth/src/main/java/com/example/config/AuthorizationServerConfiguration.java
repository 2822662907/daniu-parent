package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import javax.sql.DataSource;
import javax.annotation.Resource;
import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    //注入密码加密器
    @Autowired
    private PasswordEncoder passwordEncoder;

    //注入自定义认证对象
    @Autowired
    private UserDetailsService userDetailsService;

    //注入认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    //授权服务器端点访问权限验证方式
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()//访问服务器端点需要进行客户端身份验证   admin cli123
                .passwordEncoder(passwordEncoder)//设置客户端密码加密机制
                .tokenKeyAccess("permitAll()")//开启token检查功能
                .checkTokenAccess("permitAll()");//开启token校验功能
    }

    //客户端账号配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    //端点令牌存储方式、关联自定义认证对象、认证管理器
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(new JwtTokenStore(jwtAccessTokenConverter())) //令牌存储格式jwt
                .accessTokenConverter(jwtAccessTokenConverter())//使用jwt令牌转换器处理请求令牌
                .authenticationManager(authenticationManager)//认证管理器
                .userDetailsService(userDetailsService)//自定义认证类
                .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);//允许端点访问方法
    }

    //创建证书读取工具类
    @Bean(name = "keyProp")
    public KeyProperties keyProperties(){
        return new KeyProperties();
    }

    //引用证书读取工具类，根据对象名引用避免引用其他的对象失败
    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    //读取证书方法
    private KeyPair keyPair(){
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyProperties.getKeyStore().getLocation(),
                keyProperties.getKeyStore().getSecret().toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(keyProperties.getKeyStore().getAlias(),
                keyProperties.getKeyStore().getPassword().toCharArray());
        return keyPair;
    }

    //创建JwtAccessTokenConverter用来生成JWT令牌
    private JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //关联私钥
        jwtAccessTokenConverter.setKeyPair(keyPair());

        return jwtAccessTokenConverter;
    }

    //获取公钥的端点访问
    @Bean
    public TokenKeyEndpoint tokenKeyEndpoint(){
        return new TokenKeyEndpoint(jwtAccessTokenConverter());
    }


}

