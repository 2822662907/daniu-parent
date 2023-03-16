package com.example.controller;

import com.example.entity.Result;
import com.example.entity.StatusCode;
import com.example.service.AuthService;
import com.example.util.AuthToken;
import com.example.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/user")
public class AuthController {
    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private  String clientSecret;
    @Value("${auth.cookieDomain}")
    private  String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    private  int cookieMaxAge;
    @Autowired
    private AuthService authService;
    @PostMapping("login")
    private Result login(String username, String password, HttpServletResponse response){
        if (StringUtils.isEmpty(username))
            throw new RuntimeException("用户名不能为空");
        if (StringUtils.isEmpty(password))
            throw new RuntimeException("密码不能为空");
        AuthToken login = authService.login(username, password, clientId, clientSecret);
        //保存令牌到cookie
        CookieUtil.addCookie(response,cookieDomain,"/","Authorization",login.getAccessToken(),cookieMaxAge,false);
        return new Result(true, StatusCode.OK,"登录成功");
    }
}
