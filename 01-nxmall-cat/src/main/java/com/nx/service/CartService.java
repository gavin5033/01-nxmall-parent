package com.nx.service;

import com.nx.pojo.NxmallResult;
import com.nx.pojo.SearchEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CartService {
    //添加购物车数据
    void addCart(HttpServletRequest request,HttpServletResponse response, Long id, int num);

    //显示购物车详情页
    List<SearchEntity> showCartDetail(HttpServletRequest request);

    //合并购物车
    void mergeCart(HttpServletRequest request,HttpServletResponse response);

    //删除购物车对应商品
    NxmallResult removeCartById(HttpServletRequest request, Long id);

    //显示订单购物车详情页
    List<SearchEntity> showOrderCartDetail(HttpServletRequest request,List<Long> id);

    //修改购物车商品数量
    NxmallResult updateCartNum(Long id, int num, HttpServletRequest request);


    //删除redis中下订单的所有商品
    NxmallResult removeOrderCartByIds(List<Long> ids, String token);
}
