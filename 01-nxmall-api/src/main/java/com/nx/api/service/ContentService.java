package com.nx.api.service;

import com.nx.pojo.TbContent;

import java.util.List;

public interface ContentService {
    //根据内容分类id进行分页查询
    List<TbContent> selectContentsByPage(Long cid, int page, int rows);

    //查询总记录数
    Long count(Long cid);

    //新增
    int insertContent(TbContent tbContent);

    //查询门户打广告内容
    List<TbContent> selectContentsByCidAndNum(Long cid, int num);

}
