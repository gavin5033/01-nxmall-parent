package com.nx.manager.controller;/*
 @Author sunjiuxiang
 @Package com.nx.manage.controller
 @CreateTime 2020/8/17
*/

import com.nx.manager.service.ItemManagerService;
import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class ItemManageController {
    @Autowired
    private ItemManagerService itemManageService;

    //查询所有商品并分页显示
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDatagrid selectAllItemsByPage(int page,int rows){
        return itemManageService.selectAllItemsByPage(page, rows);
    }

    //根据商品id修改商品的状态
    @RequestMapping("/rest/item/{status}")
    @ResponseBody
    public NxmallResult updateStatusById(String ids, @PathVariable String status){
        return  itemManageService.updateStatusById(ids,status);
    }

    //文件上传
    @RequestMapping("/pic/upload")
    @ResponseBody
    public Map<String,Object> fileUpload(MultipartFile uploadFile){

        return itemManageService.fileUpload(uploadFile);
    }

    //新增
    @RequestMapping("/item/save")
    @ResponseBody
    public NxmallResult insertItem(TbItem tbItem, String desc, String itemParams){
        return itemManageService.insertItem(tbItem, desc,itemParams);
    }
}
