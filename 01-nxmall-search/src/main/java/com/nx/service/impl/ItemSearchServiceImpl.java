package com.nx.service.impl;

import com.nx.api.service.ItemCatService;
import com.nx.api.service.ItemDescService;
import com.nx.api.service.ItemService;
import com.nx.pojo.SearchEntity;
import com.nx.pojo.TbItem;
import com.nx.service.ItemSearchService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by 张益豪
 * @Classname ItemSearchServiceImpl
 * @Description TODO
 * @Date 2021/7/18 0:58
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {
    @DubboReference
    private ItemService itemService;
    @DubboReference
    private ItemCatService itemCatService;
    @DubboReference
    private ItemDescService itemDescService;

    @Value("${custom.solr.host}")
    private String url;
    @Value("${custom.solr.rows}")
    private int rows;
    //初始化Solr核心库
    @Override
    public void initSolr() throws IOException, SolrServerException {

        HttpSolrClient solrClient = new HttpSolrClient.Builder(url).build();
        List<TbItem> items = itemService.selectAllItems();
        for (TbItem item : items) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id",item.getId());
            document.addField("item_title",item.getTitle());
            document.addField("item_sell_point",item.getSellPoint());
            document.addField("item_price",item.getPrice());
            document.addField("item_image",item.getImage());
            document.addField("item_category_name",itemCatService.selectItemCatById(item.getCid()).getName());
            document.addField("item_desc",itemDescService.selectItemDescByItemId(item.getId()).getItemDesc());
            solrClient.add(document);
        }

        solrClient.commit();

    }

    @Override
    public void storeSolrIndex(Long id) throws IOException, SolrServerException {

        HttpSolrClient solrClient = new HttpSolrClient.Builder(url).build();
        TbItem item = itemService.selectItemById(id);
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id",item.getId());
        document.addField("item_title",item.getTitle());
        document.addField("item_sell_point",item.getSellPoint());
        document.addField("item_price",item.getPrice());
        document.addField("item_image",item.getImage());
        document.addField("item_category_name",itemCatService.selectItemCatById(item.getCid()).getName());
        document.addField("item_desc",itemDescService.selectItemDescByItemId(item.getId()).getItemDesc());
        solrClient.add(document);
        solrClient.commit();

    }
    //显示搜索页数据,查询索引库获取
    @Override
    public Map<String, Object> selectSolr(int page,String q) throws IOException, SolrServerException {
        Map<String, Object> map = new HashMap<>();
        List<SearchEntity> list = new ArrayList<>();

        HttpSolrClient solrClient = new HttpSolrClient.Builder(url).build();
        //创建SolrQuery查询对象
        SolrQuery query = new SolrQuery();

        //设置查询条件
        query.setQuery("item_keywords:"+q);

        //设置分页
        query.setStart(rows*(page-1));
        query.setRows(rows);

        //高亮
        //1.开启高亮
        query.setHighlight(true);
        //2.设置高亮字段
        query.addHighlightField("item_title item_sell_point");
        //3.设置高亮前缀和后缀
        query.setHighlightSimplePre("<span style='color:red'>");
        query.setHighlightSimplePost("</span>");


        //查询
        QueryResponse queryResponse = solrClient.query(query);
        SolrDocumentList documentList = queryResponse.getResults();
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        for (SolrDocument document : documentList) {
            SearchEntity entity = new SearchEntity();
            String id = document.getFieldValue("id").toString();
            entity.setId(Long.parseLong(id));

            entity.setPrice(Long.parseLong(document.getFieldValue("item_price").toString()));

            Map<String, List<String>> solrMap = highlighting.get(id);

            if(solrMap != null){
                List<String> titleList = solrMap.get("item_title");
                if(titleList  != null && titleList.size()>0){
                    entity.setTitle(titleList.get(0));
                }else{
                    entity.setTitle(document.getFieldValue("item_title").toString());
                }

                List<String> sellPointList = solrMap.get("item_sell_point");
                if(sellPointList  != null && sellPointList.size()>0){
                    entity.setSellPoint(sellPointList.get(0));
                }else{
                    entity.setSellPoint(document.getFieldValue("item_sell_point").toString());
                }


            }else{
                entity.setTitle(document.getFieldValue("item_title").toString());
                entity.setSellPoint(document.getFieldValue("item_sell_point").toString());

            }

            //图片
            Object image = document.getFieldValue("item_image");


            if(image != null){
                String imageStr = image.toString();
                String[] images = imageStr.split(",");

                entity.setImages(images);

            }else{
                String[] images = new String[1];
                entity.setImages(images);
            }

            list.add(entity);


        }

        long count = documentList.getNumFound();
        long size = count/rows;
        map.put("itemList",list);
        map.put("totalPages",count%rows==0?size:size+1);
        return map;
    }
}
