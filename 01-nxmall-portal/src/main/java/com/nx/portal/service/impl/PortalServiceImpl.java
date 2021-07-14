package com.nx.portal.service.impl;/*
 @Author sunjiuxiang
 @Package com.nx.portal.service.impl
 @CreateTime 2020/8/21
*/

import com.nx.api.service.ContentService;
import com.nx.pojo.BigAd;
import com.nx.pojo.TbContent;
import com.nx.portal.service.PortalService;
import com.nx.utils.JsonUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortalServiceImpl implements PortalService {
    @DubboReference
    private ContentService contentService;
    @Value("${custom.cid}")
    private Long cid;
    @Value("${custom.num}")
    private int num;

    @Value("${custom.redis.ad}")
    private String key;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 查询门户大广告内容
     */
    @Override
    public String showBigAd() {
        //判断redis中是否有大广告数据，如果有获取并返回；如果没有查询mysql,并放到redis中
        if(redisTemplate.hasKey(key)){
            System.out.println("从redis缓存中获取数据");
            List<BigAd> bigAds = (List<BigAd>) redisTemplate.boundHashOps(key).get(cid);
            if(bigAds!=null&&bigAds.size()!=0) return JsonUtil.objectToJson(bigAds);
        }
        String json = getAdAndSave2Redis();
        return json;
    }


    private String getAdAndSave2Redis() {
        List<TbContent> list = contentService.selectContentsByCidAndNum(cid, num);
        List<BigAd> bigAdList = new ArrayList<>();
        for (TbContent tbContent : list) {
            BigAd bigAd = new BigAd();
            bigAd.setAlt("加载失败！");
            bigAd.setHeight(240);
            bigAd.setHeightB(240);
            bigAd.setHref(tbContent.getUrl());
            bigAd.setSrc(tbContent.getPic());
            bigAd.setSrcB(tbContent.getPic2());
            bigAd.setWidth(670);
            bigAd.setWidthB(550);

            bigAdList.add(bigAd);
        }

        System.out.println("从mysql中获取数据");
//        redisTemplate.opsForValue().set(key,bigAdList);
        redisTemplate.boundHashOps(key).put(cid,bigAdList);
        String json = JsonUtil.objectToJson(bigAdList);
        return json;
    }
}
