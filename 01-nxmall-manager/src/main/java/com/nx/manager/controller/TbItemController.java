package com.nx.manager.controller;

import com.nx.manager.service.ItemManagerService;
import com.nx.pojo.EasyUIDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author by 张益豪
 * @Classname TbItemController
 * @Description TODO
 * @Date 2021/7/11 17:29
 */
@Controller
public class TbItemController {
    @Autowired
    private ItemManagerService itemManagerService;

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDatagrid getItemsByPage(int page,int rows){
        return itemManagerService.getItemsByPage(page,rows);
    }
}
