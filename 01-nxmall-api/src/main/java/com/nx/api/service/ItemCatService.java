package com.nx.api.service;

import com.nx.pojo.TbItemCat;

import java.util.List;

public interface ItemCatService {
    //根据父节点id查询对应的所有子节点
    List<TbItemCat> selectItemCatByPid(Long pid);

    //根据id查询对应的商品分类详情
    TbItemCat selectItemCatById(Long id);

    //根据pid得到商品分类总数
    Long count(Long pid);
}
