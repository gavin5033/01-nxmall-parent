package com.nx.api.service;

import com.nx.pojo.TbItemDesc;

public interface ItemDescService {
    //新增
    int insertItemDesc(TbItemDesc itemDesc);

    //根据商品id查询对应的商品描述
    TbItemDesc selectItemDescByItemId(Long itemId);
}
