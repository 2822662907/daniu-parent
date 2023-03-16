package com.example.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    //配置springsecurity拦截规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http.requestMatchers()
                .anyRequest()
                .and()
                .formLogin()
                .and()
                .csrf().disable();*/
       http.csrf().disable()
               .httpBasic()
               .and()
               .authorizeRequests()
               .anyRequest()
               .authenticated();
       // 登录配置
        http.formLogin().loginPage("/oauth/login")
                .loginProcessingUrl("user/login");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(
                "/oauth/login",
                "/css/**",
                "/data/**",
                "/fonts/**",
                "/img/**",
                "/js/**",
                "/plugins/**",
                "/login",
                "/user/logout",
                "/login.html",
                "/success.html",
                "/oauth/logout"
        );
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }


    //声明密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}