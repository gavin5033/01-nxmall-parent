package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/18
*/


import com.nx.api.service.ItemDescService;
import com.nx.mapper.TbItemDescMapper;
import com.nx.pojo.TbItemDesc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class ItemDescServiceImpl implements ItemDescService {
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    //新增
    @Override
    public int insertItemDesc(TbItemDesc itemDesc) {

        return tbItemDescMapper.insertSelective(itemDesc);
    }


    //根据商品id查询对应的商品描述
    @Override
    public TbItemDesc selectItemDescByItemId(Long itemId) {

        return tbItemDescMapper.selectByPrimaryKey(itemId);
    }
}
