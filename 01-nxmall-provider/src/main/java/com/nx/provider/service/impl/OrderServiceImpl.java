package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/31
*/


import com.nx.api.service.OrderService;
import com.nx.mapper.TbOrderMapper;
import com.nx.pojo.TbOrder;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper tbOrderMapper;
    //新增
    @Override
    public int insertOrder(TbOrder tbOrder) {
        return tbOrderMapper.insertSelective(tbOrder);
    }
}
