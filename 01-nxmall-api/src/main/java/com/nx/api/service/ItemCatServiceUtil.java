package com.nx.api.service;

import com.nx.pojo.TbItemCat;

import java.util.List;

public interface ItemCatServiceUtil {
    //对当前节点的子节点进行赋值
    List<TbItemCat> selectItemCatByPid(Long pid);
}
