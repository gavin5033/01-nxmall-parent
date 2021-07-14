package com.nx.api.service;

import com.nx.pojo.TbSeckillGoods;

import java.util.List;

public interface SeckillService {
    //查出所有秒杀商品
    public List<TbSeckillGoods> findList();

    //提交订单成功修改秒杀商品
    void updateSeckillGoods(TbSeckillGoods seckillGoods);
}
