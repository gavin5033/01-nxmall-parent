package com.nx.provider.service.impl;

import com.nx.api.service.SeckillService;
import com.nx.mapper.TbSeckillGoodsMapper;
import com.nx.mapper.TbSeckillOrderMapper;
import com.nx.pojo.TbSeckillGoods;
import com.nx.pojo.TbSeckillGoodsExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@DubboService
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;


    @Override
    public List<TbSeckillGoods> findList() {
        TbSeckillGoodsExample example=new TbSeckillGoodsExample();
        TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");//审核通过
        criteria.andStockCountGreaterThan(0);//剩余库存大于0
        criteria.andStartTimeLessThanOrEqualTo(new Date());//开始时间小于等于当前时间
        criteria.andEndTimeGreaterThan(new Date());//结束时间大于当前时间
        return seckillGoodsMapper.selectByExample(example);

    }

    @Override
    public void updateSeckillGoods(TbSeckillGoods seckillGoods) {
        seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
    }

}
