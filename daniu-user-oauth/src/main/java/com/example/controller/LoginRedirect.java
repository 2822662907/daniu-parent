package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth")
public class LoginRedirect {
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
