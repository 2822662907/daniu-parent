package com.example.service.impl;

import com.example.entity.Result;
import com.example.user.feign.UserFeign;
import com.example.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired(required = false)
    private UserFeign userFeign;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 调用用户微服务，根据用户名获得用户信息
        Result<User> userResult = userFeign.findByUsername(username);

        if(userResult!=null&&userResult.getData()!=null) {
            // 获取用户密码
            String pwd = userResult.getData().getPassword();

            // 指定用户的角色信息
            String permissions = "salesman,accountant,user,admin";

            // 返回用户详情
            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, pwd,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

            return user;
        }else{
            return null;
        }
    }
}
