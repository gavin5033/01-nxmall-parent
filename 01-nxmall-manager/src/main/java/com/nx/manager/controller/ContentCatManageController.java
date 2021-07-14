package com.nx.manager.controller;/*
 @Author sunjiuxiang
 @Package com.nx.manage.controller
 @CreateTime 2020/8/19
*/

import com.nx.manager.service.ContentCatManageService;
import com.nx.pojo.EasyUITree;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCatManageController {
    @Autowired
    private ContentCatManageService contentCatManageService;


    //根据父节点id查询所有子节点
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITree> selectCatsByPid(@RequestParam(defaultValue = "0") Long id){
        return contentCatManageService.selectCatsByPid(id);
    }

    //新增
    @RequestMapping("/content/category/create")
    @ResponseBody
    public NxmallResult insertContentCat(TbContentCategory tbContentCategory){
        return contentCatManageService.insertContentCat(tbContentCategory);
    }
    //重命名
    @RequestMapping("/content/category/update")
    @ResponseBody
    public NxmallResult updateContentCat(TbContentCategory tbContentCategory){
        return contentCatManageService.updateContentCat(tbContentCategory);
    }
    //删除
    @RequestMapping("/content/category/delete")
    @ResponseBody
    public NxmallResult deleteContentCat(TbContentCategory tbContentCategory){
        return contentCatManageService.deleteContentCat(tbContentCategory);
    }
}
