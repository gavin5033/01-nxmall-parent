package com.nx.service;/*
 @Author sunjiuxiang
 @Package com.nx.service
 @CreateTime 2020/8/24
*/

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Map;

public interface ItemSearchService {
    //初始化Solr核心库
    void initSolr() throws IOException, SolrServerException;

    //显示搜索页数据,查询索引库获取
    Map<String,Object> selectSolr(int page,String q) throws IOException, SolrServerException;

    //跟新索引库
    public void storeSolrIndex(Long id) throws IOException, SolrServerException ;
}
