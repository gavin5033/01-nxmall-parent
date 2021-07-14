package com.nx.manager.controller;/*
 @Author sunjiuxiang
 @Package com.nx.manage.controller
 @CreateTime 2020/8/18
*/

import com.nx.manager.service.ItemCatManageService;
import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatManageController {
    @Autowired
    private ItemCatManageService itemCatManageService;

    //根据父节点id查询对应的所有子节点
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITree> selectItemCatByPid(@RequestParam(defaultValue = "0") Long id){
        return itemCatManageService.selectItemCatByPid(id);
    }

    @RequestMapping("/item/cat/list/{parentId}")
    @ResponseBody
    public EasyUIDatagrid showItemCatByPage(int page, int rows,@PathVariable(name="parentId") Long id){
        return itemCatManageService.findItemCatByPid(id);
    }
}
