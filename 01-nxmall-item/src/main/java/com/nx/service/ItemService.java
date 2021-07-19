package com.nx.service;

import com.nx.pojo.CatMenu;
import com.nx.pojo.SearchEntity;

public interface ItemService {
    //将服务提供方数据封装成portal需要的格式
    CatMenu selectItemCat();

    //根据商品id查询对应的商品详情
    SearchEntity selectItemById(Long id);

    //根据商品id查询商品描述item_desc
    String selectItemDescById(Long id);

    //根据商品id查询商品规格参数param_data
    String selectParamDataByItemId(Long id);

    //减少库存
    void reduceStock(Long itemId, int num);
}
