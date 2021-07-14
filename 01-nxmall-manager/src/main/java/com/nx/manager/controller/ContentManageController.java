package com.nx.manager.controller;/*
 @Author sunjiuxiang
 @Package com.nx.manage.controller
 @CreateTime 2020/8/19
*/

import com.nx.manager.service.ContentManageService;
import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentManageController {
    @Autowired
    private ContentManageService contentManageService;


    //根据内容分类id分页查询
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDatagrid showContentsByPage(Long categoryId,int page,int rows){
        return contentManageService.selectContentsByPage(categoryId, page, rows);
    }

    //新增
    @RequestMapping("/content/save")
    @ResponseBody
    public NxmallResult insertContent(TbContent tbContent){
        return contentManageService.insertContent(tbContent);
    }
}
