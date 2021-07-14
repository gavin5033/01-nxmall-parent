package com.nx.manager.service;/*
 @Author sunjiuxiang
 @Package com.nx.manage.service
 @CreateTime 2020/8/18
*/

import com.nx.pojo.EasyUIDatagrid;
import com.nx.pojo.EasyUITree;

import java.util.List;

public interface ItemCatManageService {
    //根据父节点id查询对应的所有子节点
    List<EasyUITree> selectItemCatByPid(Long pid);

    EasyUIDatagrid findItemCatByPid(Long id);
}
