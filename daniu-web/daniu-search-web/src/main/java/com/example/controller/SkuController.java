package com.example.controller;


import com.example.search.feign.SearchSkuFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
@Controller
@RequestMapping(value = "/search")
public class SkuController {

    @Autowired
    private SearchSkuFeign searchSkuFeign;

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/list")
    // 发送请求，远程调用搜索微服务，请求结果交给模板渲染
    public String search(@RequestParam(required = false) Map searchMap, Model model){
        //daniu-search-service微服务
        Map resultMap = searchSkuFeign.search(searchMap);
        // 搜索结果
        model.addAttribute("result",resultMap);
        // 搜索条件
        model.addAttribute("searchMap",searchMap);
        //拼接url
        String url = this.setUrl(searchMap);
        model.addAttribute("url",url);
        return "search";
    }

    private String setUrl(Map<String,String> searchMap) {
        String url = "/search/list";
        if (searchMap!=null&&searchMap.size()>0){
            url+="?";
            for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
                String key = stringStringEntry.getKey();// keywords / brand  / category
                String value = stringStringEntry.getValue();//华为  / 华为  / 笔记本
                if(key.equals("pageNum")){
                    continue;
                }
                url+=key+"="+value+"&";
            }

            //去掉多余的&
            if(url.lastIndexOf("&")!=-1){
                url =  url.substring(0,url.lastIndexOf("&"));
            }

        }
        return url;
    }

}
