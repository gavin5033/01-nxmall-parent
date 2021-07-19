package com.nx.controller;

import com.nx.pojo.NxmallResult;
import com.nx.service.ItemSearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class SearchController {
    @Autowired
    private ItemSearchService itemSearchService;
    //初始化Solr核心库
    @RequestMapping(value = "/initSolr",produces = "application/json;charset='utf-8'")
    @ResponseBody
    public String initSolr(){
        String time ="";
        try {
            long start = System.currentTimeMillis();
            itemSearchService.initSolr();
            long end = System.currentTimeMillis();
            time = "初始化Solr耗时为："+(end-start)/1000/60+"分钟";

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**s
     * 商品添加的时候调用改接口更新索引库
     * @param id
     * @return
     */
    @RequestMapping("/search/store/{id}")
    public NxmallResult storeSolrIndex(@PathVariable("id") Long id){
        NxmallResult result =  new NxmallResult();
        try {
            itemSearchService.storeSolrIndex(id);
            result.setStatus(200);
        }catch (Exception e){
            e.printStackTrace();
           result.setStatus(500);
        }
        return result;

    }



}
