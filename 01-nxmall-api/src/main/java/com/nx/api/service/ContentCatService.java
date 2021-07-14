package com.nx.api.service;

import com.nx.pojo.TbContentCategory;

import java.util.List;

public interface ContentCatService {
    //根据父节点id查询所有子节点
    List<TbContentCategory> selectContentCatsByPid(Long pid);

    //新增
    Long insertContentCat(TbContentCategory tbContentCategory);

    //修改
    int updateContentCat(TbContentCategory tbContentCategory);

    //根据id查询对应详情
    TbContentCategory selectContentCatById(Long id);

    //根据条件查询对应内容分类
    List<TbContentCategory> selectContentCatByCondition(TbContentCategory tbContentCategory);

    //根据pid和status进行查询
    List<TbContentCategory> selectContentCatByPidAndStatus(TbContentCategory tbContentCategory);
}
