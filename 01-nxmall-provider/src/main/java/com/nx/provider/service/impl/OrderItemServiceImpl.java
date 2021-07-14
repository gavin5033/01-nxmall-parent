package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/31
*/


import com.nx.api.service.OrderItemService;
import com.nx.mapper.TbOrderItemMapper;
import com.nx.pojo.TbOrderItem;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Override
    public int insertOrderItem(TbOrderItem tbOrderItem) {
        return tbOrderItemMapper.insertSelective(tbOrderItem);
    }
}
