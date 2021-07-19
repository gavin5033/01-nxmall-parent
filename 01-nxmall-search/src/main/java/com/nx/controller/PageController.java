package com.nx.controller;

import com.nx.service.ItemSearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
public class PageController {
    @Autowired
    private ItemSearchService itemSearchService;
    //跳转搜索页面
    @RequestMapping({"/search.html","/search"})
    public String showSearchPage(@RequestParam(defaultValue = "1") int page, String q, Model model){
        try {
            Map<String, Object> map = itemSearchService.selectSolr(page, q);
            model.addAttribute("itemList",map.get("itemList"));
            model.addAttribute("totalPages",map.get("totalPages"));
            model.addAttribute("query",q);
            model.addAttribute("page",page);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return "search";
    }
}
