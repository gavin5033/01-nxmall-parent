package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/19
*/


import com.nx.api.service.ItemParamItemService;
import com.nx.mapper.TbItemParamItemMapper;
import com.nx.pojo.TbItemParamItem;
import com.nx.pojo.TbItemParamItemExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class ItemParamItemServiceImpl implements ItemParamItemService {
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    //新增
    @Override
    public int insertItemParamItem(TbItemParamItem tbItemParamItem) {

        return tbItemParamItemMapper.insertSelective(tbItemParamItem);
    }


    //根据商品id查询对应的商品规格参数
    @Override
    public TbItemParamItem selectItemParamItemByItemId(Long id) {
        TbItemParamItemExample example = new TbItemParamItemExample();
        example.createCriteria().andItemIdEqualTo(id);
        List<TbItemParamItem> tbItemParamItems = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if(tbItemParamItems != null && tbItemParamItems.size()>0){
            return tbItemParamItems.get(0);
        }
        return null;
    }
}
