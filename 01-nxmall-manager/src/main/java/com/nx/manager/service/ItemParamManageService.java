package com.nx.manager.service;/*
 @Author sunjiuxiang
 @Package com.nx.manage.service
 @CreateTime 2020/8/18
*/

import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbItemParam;

public interface ItemParamManageService {
    //查询所有商品规格参数并分页显示
    EasyUIDatagrid showItemParamByPage(int page,int rows);

    //根据商品分类Id查询对应的商品规格参数
    NxmallResult selectItemParamByCatId(Long catId);

    //新增
    NxmallResult insertItemParam(TbItemParam tbItemParam);

    //删除
    NxmallResult deleteItemParamById(String ids);

}
