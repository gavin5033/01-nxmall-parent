package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/18
*/


import com.nx.api.service.ItemCatService;
import com.nx.mapper.TbItemCatMapper;
import com.nx.pojo.TbItemCat;
import com.nx.pojo.TbItemCatExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    //根据父节点id查询对应的所有子节点
    @Override
    public List<TbItemCat> selectItemCatByPid(Long pid) {
        //创建条件构造器
        TbItemCatExample catExample = new TbItemCatExample();
        //设置排序
        catExample.setOrderByClause("sort_order asc");
        //添加条件,criteria为条件构造体
        TbItemCatExample.Criteria criteria = catExample.createCriteria();
        criteria.andParentIdEqualTo(pid).andStatusEqualTo(1);

        return tbItemCatMapper.selectByExample(catExample);
    }


    //根据id查询对应的商品分类详情
    @Override
    public TbItemCat selectItemCatById(Long id) {

        return tbItemCatMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long count(Long pid) {
        TbItemCatExample example = new TbItemCatExample();
        if(pid != 0){
            example.createCriteria().andParentIdEqualTo(pid);
        }

        return tbItemCatMapper.countByExample(example);
    }
}
