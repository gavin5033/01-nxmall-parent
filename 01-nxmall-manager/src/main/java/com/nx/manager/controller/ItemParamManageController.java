package com.nx.manager.controller;

import com.nx.manager.service.ItemParamManageService;
import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbItemParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemParamManageController {
    @Autowired
    private ItemParamManageService itemParamManageService;

    //查询所有商品规格参数并分页显示
    @RequestMapping("/item/param/list")
    @ResponseBody
    public EasyUIDatagrid showItemParamByPage(int page,int rows){
        return itemParamManageService.showItemParamByPage(page, rows);
    }

    //根据商品分类Id查询对应的商品规格参数
    @RequestMapping("/item/param/query/itemcatid/{catId}")
    @ResponseBody
    public NxmallResult selectItemParamByCatId(@PathVariable Long catId){
        return itemParamManageService.selectItemParamByCatId(catId);
    }

    //新增
    @RequestMapping("/item/param/save/{catId}")
    @ResponseBody
    public NxmallResult insertItemParam(@PathVariable Long catId, TbItemParam tbItemParam){
        tbItemParam.setItemCatId(catId);
        return itemParamManageService.insertItemParam(tbItemParam);
    }
    //删除
    @RequestMapping("/item/param/delete")
    @ResponseBody
    public NxmallResult deleteItemParamById(String ids){
        return itemParamManageService.deleteItemParamById(ids);
    }
}
