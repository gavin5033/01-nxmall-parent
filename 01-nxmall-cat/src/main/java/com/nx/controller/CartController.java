package com.nx.controller;

import com.nx.pojo.NxmallResult;
import com.nx.pojo.SearchEntity;
import com.nx.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    //显示购物车添加成功页面
    @RequestMapping("/cart/add/{itemId}.html")
    public String showCartSuccessPage(HttpServletRequest request, HttpServletResponse response, @PathVariable Long itemId, int num){
        cartService.addCart(request, response, itemId, num);
        return "cartSuccess";
    }
    //显示购物车详情页
    @RequestMapping("/cart/cart.html")
    public String showCartDetailPage(HttpServletRequest request, Model model){
        List<SearchEntity> list = cartService.showCartDetail(request);
        model.addAttribute("cartList",list);
        return "cart";
    }

    //合并购物车
    @RequestMapping("/cart/merge")
    @CrossOrigin
    public String mergeCart(String url,HttpServletRequest request,HttpServletResponse response){
        System.out.println("接收页面请求路径为："+url);
        cartService.mergeCart(request, response);
        return "redirect:"+url;
    }


    //删除购物车对应商品
    @RequestMapping("/cart/delete/{id}.action")
    @ResponseBody
    public NxmallResult removeCartById(HttpServletRequest request, @PathVariable Long id){
        return cartService.removeCartById(request, id);
    }

    //显示订单购物车页面
    @RequestMapping("/cart/order-cart.html")
    public String showOrderCartPage(Model model,HttpServletRequest request,@RequestParam List<Long> id){
        List<SearchEntity> list = cartService.showOrderCartDetail(request, id);
        model.addAttribute("cartList",list);
        return "order-cart";
    }

    //修改购物车商品数量
    @RequestMapping("/cart/update/num/{itemId}/{num}.action")
    @ResponseBody
    public NxmallResult updateCartNum(HttpServletRequest request, @PathVariable Long itemId, @PathVariable int num){
        return cartService.updateCartNum(itemId,num,request);
    }

    //删除redis中下订单的所有商品
    @RequestMapping("/cart/delete/{token}")
    @ResponseBody
    public NxmallResult removeOrderCartByIds(@RequestBody List<Long> ids, @PathVariable String token){
        return cartService.removeOrderCartByIds(ids,token);
    }
}
