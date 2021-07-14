package com.nx.api.service;

import com.nx.pojo.TbItemParamItem;

public interface ItemParamItemService {
    //新增
    int insertItemParamItem(TbItemParamItem tbItemParamItem);

    //根据商品id查询对应的商品规格参数
    TbItemParamItem selectItemParamItemByItemId(Long id);
}
