package com.nx.controller;

import com.nx.pojo.CatMenu;
import com.nx.pojo.SearchEntity;
import com.nx.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;
    //将服务提供方数据封装成portal需要的格式并返回给portal
    @RequestMapping("/rest/itemcat/all")
    @ResponseBody
    @CrossOrigin()
    public CatMenu showItemCat(){
        return itemService.selectItemCat();
    }

    //显示商品详情页面，并根据商品id显示商品详情
    @RequestMapping("/item/{itemId}.html")
    public String selectItemById(@PathVariable Long itemId, Model model){
        SearchEntity searchEntity = itemService.selectItemById(itemId);
        model.addAttribute("item",searchEntity);
        return "item";

    }


    //根据商品id查询商品描述item_desc
    //@RequestMapping(value = "/item/desc/{itemId}.html",produces = "text/html;charset='utf-8'")
    @RequestMapping(value = "/item/desc/{itemId}.html")
    @ResponseBody
    public String selectItemDescById(@PathVariable Long itemId){
        return itemService.selectItemDescById(itemId);
    }
    //根据商品id查询商品规格参数param_data
    @RequestMapping(value = "/item/param/{itemId}.html")
    @ResponseBody
    public String selectParamDataByItemId(@PathVariable Long itemId){
        return itemService.selectParamDataByItemId(itemId);
    }
}
