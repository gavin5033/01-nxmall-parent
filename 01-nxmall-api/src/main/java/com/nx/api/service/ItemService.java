package com.nx.api.service;

import com.nx.pojo.TbItem;

import java.util.Date;
import java.util.List;

/**
 * @author by 张益豪
 * @Classname ItemService
 * @Description TODO
 * @Date 2021/7/11 16:09
 */
public interface ItemService {
    //显示当前页面商品详情
    List<TbItem> selectItemsByPage(int page,int rows);

    //查询商品总记录数
    Long countItem();

    //更新商品状态
    int updateItemStatusById(Long id,String status);

    //新增
    int insertItem(TbItem tbItem);

    //查询所有状态正常的商品
    List<TbItem> selectAllItems();

    //根据商品id查询对应的商品详情
    TbItem selectItemById(Long id);

    //根据商品id修改商品库存
    int updateNumById(Long id, int num, Date date);
}
