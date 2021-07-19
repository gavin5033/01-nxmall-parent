package com.nx.service;

import com.nx.pojo.OrderParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface OrderConsumerService {
    //新增
    Map<String,Object> addOrder(OrderParam orderParam, HttpServletRequest request);

    void addOrder(Map param);
}
