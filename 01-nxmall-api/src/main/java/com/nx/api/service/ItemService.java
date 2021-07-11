package com.nx.api.service;

import com.nx.pojo.TbItem;

import java.util.List;

/**
 * @author by 张益豪
 * @Classname ItemService
 * @Description TODO
 * @Date 2021/7/11 16:09
 */
public interface ItemService {
    //显示当前页面商品详情
    List<TbItem> selectItemByPage(int page, int rows);

    //查询商品总记录数
    Long countItem();
}
