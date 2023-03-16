package com.example.item.controller;
import com.example.item.service.PageService;
import com.example.entity.Result;
import com.example.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
public class PageController {
    @Autowired(required = false)
    private PageService pageService;
    /**
     * 生成静态页面
     * @param id
     * @return
     */
    @RequestMapping("createhtml/{id}")
    public Result creatPageHtml(@PathVariable(name="id")  Long id){
        pageService.createPageHtml(id);
        return new Result(true, StatusCode.OK,"静态页面生成成功");
    }
}
