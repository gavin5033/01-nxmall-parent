package com.nx.manager.service;/*
 @Author sunjiuxiang
 @Package com.nx.manage.service
 @CreateTime 2020/8/19
*/

import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbContent;

public interface ContentManageService {
    //根据内容分类id分页查询
    EasyUIDatagrid selectContentsByPage(Long cid, int page, int rows);

    //新增
    NxmallResult insertContent(TbContent tbContent);
}
