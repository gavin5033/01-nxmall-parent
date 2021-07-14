package com.nx.manager.service.impl;/*
 @Author sunjiuxiang
 @Package com.nx.manage.service.impl
 @CreateTime 2020/8/18
*/

import com.nx.api.service.ItemCatService;
import com.nx.api.service.ItemParamService;
import com.nx.manager.pojo.TbItemParamCat;
import com.nx.manager.service.ItemParamManageService;
import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbItemParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemParamManageServiceImpl implements ItemParamManageService {
    @DubboReference
    private ItemParamService itemParamService;
    @DubboReference
    private ItemCatService itemCatService;
    @Override
    public EasyUIDatagrid showItemParamByPage(int page, int rows) {
        EasyUIDatagrid datagrid = new EasyUIDatagrid();
        List<TbItemParam> params = itemParamService.selectItemParamByPage(page, rows);
        List<TbItemParamCat>  paramCatList = new ArrayList<>();
        Long count = itemParamService.countItemParam();

        for (TbItemParam param : params) {
            TbItemParamCat paramCat = new TbItemParamCat();
            BeanUtils.copyProperties(param,paramCat);

            String catName = itemCatService.selectItemCatById(param.getItemCatId()).getName();
            paramCat.setItemCatName(catName);

            paramCatList.add(paramCat);

        }

        datagrid.setTotal(count);
        datagrid.setRows(paramCatList);
        return datagrid;
    }


    //根据商品分类Id查询对应的商品规格参数
    @Override
    public NxmallResult selectItemParamByCatId(Long catId) {
        NxmallResult er = new NxmallResult();
        TbItemParam tbItemParam = itemParamService.selectItemParamByCatId(catId);
        if(tbItemParam != null){
            er.setStatus(200);
            er.setData(tbItemParam);
        }
        return er;
    }


    //新增
    @Override
    public NxmallResult insertItemParam(TbItemParam tbItemParam) {
        NxmallResult er = new NxmallResult();
        Date date = new Date();
        tbItemParam.setCreated(date);
        tbItemParam.setUpdated(date);
        int index = itemParamService.insertItemParam(tbItemParam);

        if(index >0){
            er.setStatus(200);
        }
        return er;
    }


    //删除
    @Override
    @Transactional(rollbackFor = Exception.class)
    public NxmallResult deleteItemParamById(String ids) {
        NxmallResult er = new NxmallResult();
        String[] idsStr = ids.split(",");
        int index = 0;
        for (String idStr : idsStr) {
            long id = Long.parseLong(idStr);
            index  += itemParamService.deleteItemParamById(id);
        }

        if(index ==idsStr.length){
            er.setStatus(200);
        }
        return er;
    }
}
