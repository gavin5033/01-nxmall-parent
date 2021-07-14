package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/31
*/

import com.nx.api.service.OrderShippingService;
import com.nx.mapper.TbOrderShippingMapper;
import com.nx.pojo.TbOrderShipping;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class OrderShippingServiceImpl implements OrderShippingService {
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Override
    public int insertOrderShipping(TbOrderShipping tbOrderShipping) {
        return tbOrderShippingMapper.insertSelective(tbOrderShipping);
    }
}
