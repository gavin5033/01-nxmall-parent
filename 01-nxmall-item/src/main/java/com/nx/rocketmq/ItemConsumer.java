package com.nx.rocketmq;

import com.nx.pojo.TbOrderItem;
import com.nx.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RocketMQMessageListener(topic = "tx_order_msg", consumerGroup = "tx_order")
public class ItemConsumer implements RocketMQListener<List<TbOrderItem>> {
    @Autowired
    private ItemService itemService;
    // 监听消息进行减库存
    @Override
    public void onMessage(List<TbOrderItem> orderItems ) {
        for (TbOrderItem orderItem : orderItems) {
            itemService.reduceStock(Long.parseLong(orderItem.getItemId()),orderItem.getNum());
        }
    }
}
