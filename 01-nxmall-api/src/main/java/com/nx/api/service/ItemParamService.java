package com.nx.api.service;

import com.nx.pojo.TbItemParam;

import java.util.List;

public interface ItemParamService {
    //查询商品规格参数并分页，查询当前页面数据数据详情
    List<TbItemParam> selectItemParamByPage(int page, int rows);
    //查询商品规格参数总数
    Long countItemParam();

    //根据商品分类Id查询对应的商品规格参数
    TbItemParam selectItemParamByCatId(Long catId);

    //新增
    int insertItemParam(TbItemParam tbItemParam);

    //删除
    int deleteItemParamById(Long id);
}
