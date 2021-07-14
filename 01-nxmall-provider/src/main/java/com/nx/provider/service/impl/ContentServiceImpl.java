package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/19
*/


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nx.api.service.ContentService;
import com.nx.mapper.TbContentMapper;
import com.nx.pojo.TbContent;
import com.nx.pojo.TbContentExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;

    //查询当前页面详情
    @Override
    public List<TbContent> selectContentsByPage(Long cid, int page, int rows) {
        PageHelper.startPage(page,rows);
        TbContentExample example = new TbContentExample();
        if(cid != 0){
            example.createCriteria().andCategoryIdEqualTo(cid);
        }

        List<TbContent> tbContents = tbContentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContents);
        return pageInfo.getList();
    }


    //查询总数
    @Override
    public Long count(Long cid) {
        TbContentExample example = new TbContentExample();
        if(cid != 0){
            example.createCriteria().andCategoryIdEqualTo(cid);
        }

        return tbContentMapper.countByExample(example);
    }

    //新增
    @Override
    public int insertContent(TbContent tbContent) {

        return tbContentMapper.insertSelective(tbContent);
    }


    //查询门户打广告内容
    @Override
    public List<TbContent> selectContentsByCidAndNum(Long cid, int num) {
        PageHelper.startPage(1,num);
        TbContentExample example = new TbContentExample();
        example.setOrderByClause("updated desc");
        example.createCriteria().andCategoryIdEqualTo(cid);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);

        return pageInfo.getList();
    }
}
