package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/19
*/


import com.nx.api.service.ContentCatService;
import com.nx.mapper.TbContentCategoryMapper;
import com.nx.pojo.TbContentCategory;
import com.nx.pojo.TbContentCategoryExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class ContentCatServiceImpl implements ContentCatService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    //根据父节点id查询所有子节点
    @Override
    public List<TbContentCategory> selectContentCatsByPid(Long pid) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        //排序
        example.setOrderByClause("sort_order asc");
        example.createCriteria().andParentIdEqualTo(pid).andStatusEqualTo(1);

        return tbContentCategoryMapper.selectByExample(example);
    }


    //新增
    @Override
    public Long insertContentCat(TbContentCategory tbContentCategory) {

        int index = tbContentCategoryMapper.insertSelective(tbContentCategory);

        //新增成功后将新增的对象id返回
        if(index >0){
            return tbContentCategory.getId();
        }else{
            return null;
        }

    }


    //修改
    @Override
    public int updateContentCat(TbContentCategory tbContentCategory) {

        return tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
    }

    //根据id查询对应详情
    @Override
    public TbContentCategory selectContentCatById(Long id) {

        return tbContentCategoryMapper.selectByPrimaryKey(id);
    }


    //根据条件查询对应内容分类
    @Override
    public List<TbContentCategory> selectContentCatByCondition(TbContentCategory tbContentCategory) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(tbContentCategory.getParentId()).andNameEqualTo(tbContentCategory.getName());

        return tbContentCategoryMapper.selectByExample(example);
    }


    //根据pid和status进行查询
    @Override
    public List<TbContentCategory> selectContentCatByPidAndStatus(TbContentCategory tbContentCategory) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(tbContentCategory.getParentId()).andStatusEqualTo(tbContentCategory.getStatus());

        return tbContentCategoryMapper.selectByExample(example);
    }
}
