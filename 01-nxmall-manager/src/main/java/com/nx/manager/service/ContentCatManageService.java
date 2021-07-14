package com.nx.manager.service;/*
 @Author sunjiuxiang
 @Package com.nx.manage.service
 @CreateTime 2020/8/19
*/

import com.nx.pojo.EasyUITree;
import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbContentCategory;

import java.util.List;

public interface ContentCatManageService {
    //根据父节点id查询所有子节点
    List<EasyUITree> selectCatsByPid(Long pid);

    //新增
    NxmallResult insertContentCat(TbContentCategory tbContentCategory);

    //重命名
    NxmallResult updateContentCat(TbContentCategory tbContentCategory);

    //删除
    NxmallResult deleteContentCat(TbContentCategory tbContentCategory);
}
