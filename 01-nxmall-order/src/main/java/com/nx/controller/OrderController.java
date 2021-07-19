package com.nx.controller;

import com.nx.pojo.OrderParam;
import com.nx.service.OrderConsumerService;
import com.nx.utils.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class OrderController {
    @Autowired
    private OrderConsumerService orderConsumerService;
    //显示订单成功页
    @RequestMapping("/order/create.html")
    public String showOrderSuccessPage(OrderParam orderParam, HttpServletRequest request, Model model){
        String orderId = IDUtil.nextId()+"";
        orderParam.setOrderId(orderId);
        Map<String, Object> map = orderConsumerService.addOrder(orderParam, request);
        model.addAttribute("orderId",orderId);
        model.addAttribute("payment",map.get("payment"));
        model.addAttribute("date",map.get("date"));
        return "success";
    }
}
