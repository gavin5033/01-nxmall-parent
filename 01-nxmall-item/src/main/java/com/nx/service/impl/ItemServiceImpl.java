package com.nx.service.impl;

import com.nx.api.service.ItemCatServiceUtil;
import com.nx.api.service.ItemDescService;
import com.nx.api.service.ItemParamItemService;
import com.nx.pojo.*;
import com.nx.service.ItemService;
import com.nx.utils.JsonUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @DubboReference
    private com.nx.api.service.ItemService itemService;
    @DubboReference
    private ItemCatServiceUtil itemCatServiceUtil;

    @DubboReference
    private ItemDescService itemDescService;

    @DubboReference
    private ItemParamItemService itemParamItemService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Value("${custom.redis.portalNavKey}")
    private String key;

    @Value("${custom.redis.item}")
    private String itemKey;

    //将服务提供方数据封装成portal需要的格式
    @Override
    public CatMenu selectItemCat() {
        //判断redis缓存中是否有对应的数据，如果有从redis获取；如果没有从mysql中获取，并存入redis中
        if(redisTemplate.hasKey(key)){
            System.out.println("从redis缓存获取数据");
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<CatMenu>(CatMenu.class));
            CatMenu cm = (CatMenu) redisTemplate.opsForValue().get(key);
            return cm;
        }
        System.out.println("从mysql中获取数据");
        List<TbItemCat> catList = itemCatServiceUtil.selectItemCatByPid(0L);
        List<Object> allMenuCatList = getAllMenuCat(catList);
        CatMenu cm = new CatMenu();
        cm.setData(allMenuCatList);
        redisTemplate.opsForValue().set(key,cm);
        return cm;
    }




    //该方法真正完成数据封装（方法递归调用）
    public List<Object> getAllMenuCat(List<TbItemCat> catList){
        List<Object> catsList = new ArrayList<>();
        for (TbItemCat tbItemCat : catList) {
            PortalMenuCat pmc = new PortalMenuCat();

            //如果为父节点
            if(tbItemCat.getIsParent()){
                pmc.setU("/products/"+tbItemCat.getId()+".html");
                pmc.setN(tbItemCat.getName());
                pmc.setI(getAllMenuCat(tbItemCat.getChildren()));

                catsList.add(pmc);
            }else{
                catsList.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
            }
        }
        return catsList;

    }


    /**
     * 方案一：伪静态化方式
     *    路径以.html结尾，优点逻辑简单，缺点，堆Redis的容量要求高
     *  方案二：真的静态化方式
     *     预先将每个商品的详情页生成好html
     *     定时任务、freeMarker模块生成、Nginx静态代理将访问的html页面的请求代理到html
     *     manage进行商品维护时需要重新生成静态化页面，使用MQ异步来完成静态化同步
     * @param id
     * @return
     */

    @Override
    public SearchEntity selectItemById(Long id) {
        //判断redis中是否有对应的数据
        String itemRedisKey = itemKey+id;
        if(redisTemplate.hasKey(itemRedisKey)){
            System.out.println("从redis中获取商品数据");
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<SearchEntity>(SearchEntity.class));
            SearchEntity searchEntity = (SearchEntity) redisTemplate.opsForValue().get(itemRedisKey);
            return searchEntity;
        }
        System.out.println("从mysql中获取商品数据");
        TbItem tbItem = itemService.selectItemById(id);
        SearchEntity searchEntity = new SearchEntity();
        BeanUtils.copyProperties(tbItem,searchEntity);
        String image = tbItem.getImage();

        searchEntity.setImages((image!=null && !"".equals(image))?image.split(","):new String[1]);

        //将mysql查询的数据放到redis中
        redisTemplate.opsForValue().set(itemRedisKey,searchEntity);
        return searchEntity;
    }


    //根据商品id查询商品描述item_desc
    @Override
    public String selectItemDescById(Long id) {
        TbItemDesc tbItemDesc = itemDescService.selectItemDescByItemId(id);
        return tbItemDesc.getItemDesc();
    }


    //根据商品id查询商品规格参数param_data
    @Override
    public String selectParamDataByItemId(Long id) {
        //从mysql中查询对应的商品规格参数
        TbItemParamItem tbItemParamItem = itemParamItemService.selectItemParamItemByItemId(id);
        String paramData = tbItemParamItem.getParamData();
        List<ParamData> paramDataList = JsonUtil.jsonToList(paramData, ParamData.class);
        StringBuilder builder = new StringBuilder();
        for (ParamData data : paramDataList) {
            builder.append("<table style='color:gray;'>");
            List<MyParam> params = data.getParams();
            for (int i = 0; i < params.size(); i++) {
                builder.append("<tr>");
                if(i == 0){
                    builder.append("<td>"+data.getGroup()+"</td>");
                }else{
                    builder.append("<td>"+"</td>");
                }
                builder.append("<td>"+params.get(i).getK()+"</td>");
                builder.append("<td>"+params.get(i).getV()+"</td>");
                builder.append("</tr>");
            }
            builder.append("</table>");
            builder.append("<hr style='color:gray'>");
        }
        return builder.toString();
    }

    @Override
    public void reduceStock(Long itemId, int num) {
        itemService.updateNumById(itemId,num,new Date());
    }
}
