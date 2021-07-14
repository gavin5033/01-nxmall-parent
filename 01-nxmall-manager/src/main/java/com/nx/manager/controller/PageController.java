package com.nx.manager.controller;/*
 @Author sunjiuxiang
 @Package com.nx.manage.controller
 @CreateTime 2020/8/17
*/

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    //显示首页index
    @RequestMapping("/")
    public String showIndexs(){
        return "index";
    }

    //显示其他页面
    @RequestMapping("/{page}")
    public String showOtherPage(@PathVariable String page){
        return page;
    }
}
