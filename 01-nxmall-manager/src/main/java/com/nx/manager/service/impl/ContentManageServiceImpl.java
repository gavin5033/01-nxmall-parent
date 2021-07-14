package com.nx.manager.service.impl;

import com.nx.api.service.ContentService;
import com.nx.manager.service.ContentManageService;
import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbContent;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentManageServiceImpl implements ContentManageService {
    @DubboReference
    private ContentService contentService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Value("${custom.redis.ad}")
    private String key;

    //根据内容分类id进行分页查询
    @Override
    public EasyUIDatagrid selectContentsByPage(Long cid, int page, int rows) {
        List<TbContent> tbContents = contentService.selectContentsByPage(cid, page, rows);
        Long count = contentService.count(cid);

        EasyUIDatagrid datagrid = new EasyUIDatagrid();
        datagrid.setTotal(count);
        datagrid.setRows(tbContents);
        return datagrid;
    }


    //新增
    @Override
    public NxmallResult insertContent(TbContent tbContent) {
        NxmallResult er= new NxmallResult();
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        int index = contentService.insertContent(tbContent);
        System.out.println("新增的值为："+index);
        if(index > 0){
            //实现redis的数据同步
            //判断redis中是否有对应的数据
            System.out.println("key的值为："+key);
            sysContentToRedis(tbContent);
            er.setStatus(200);
        }
        return er;
    }

    private void sysContentToRedis(TbContent tbContent) {
        if(redisTemplate.hasKey(key)){
//            redisTemplate.delete(key);
            if(redisTemplate.boundHashOps(key).hasKey(tbContent.getCategoryId())){
              redisTemplate.boundHashOps(key).delete(tbContent.getCategoryId());
            }

        }
    }
}
