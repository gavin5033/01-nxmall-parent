package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/18
*/


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nx.api.service.ItemParamService;
import com.nx.mapper.TbItemParamMapper;
import com.nx.pojo.TbItemParam;
import com.nx.pojo.TbItemParamExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class ItemParamServiceImpl implements ItemParamService {
    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    //查询商品规格参数并分页，查询当前页面数据数据详情
    @Override
    public List<TbItemParam> selectItemParamByPage(int page, int rows) {
        PageHelper.startPage(page, rows);
        List<TbItemParam> params = tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
        PageInfo<TbItemParam> pageInfo = new PageInfo<>(params);
        return pageInfo.getList();
    }

    //查询商品规格参数总数
    @Override
    public Long countItemParam() {

        return tbItemParamMapper.countByExample(new TbItemParamExample());
    }


    //根据商品分类Id查询对应的商品规格参数
    @Override
    public TbItemParam selectItemParamByCatId(Long catId) {
        TbItemParamExample example = new TbItemParamExample();
        example.createCriteria().andItemCatIdEqualTo(catId);
        List<TbItemParam> params = tbItemParamMapper.selectByExampleWithBLOBs(example);
        if(params != null && params.size()>0){
            return params.get(0);
        }
        return null;
    }


    //新增
    @Override
    public int insertItemParam(TbItemParam tbItemParam) {

        return tbItemParamMapper.insertSelective(tbItemParam);
    }


    //删除
    @Override
    public int deleteItemParamById(Long id) {
        return tbItemParamMapper.deleteByPrimaryKey(id);
    }
}
