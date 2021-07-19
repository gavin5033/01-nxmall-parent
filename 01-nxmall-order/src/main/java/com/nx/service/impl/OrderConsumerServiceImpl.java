package com.nx.service.impl;

import com.nx.api.service.ItemService;
import com.nx.api.service.OrderItemService;
import com.nx.api.service.OrderService;
import com.nx.api.service.OrderShippingService;
import com.nx.pojo.*;
import com.nx.service.OrderConsumerService;
import com.nx.utils.CookieUtil;
import com.nx.utils.HttpClientUtil;
import com.nx.utils.IDUtil;
import com.nx.utils.JsonUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class OrderConsumerServiceImpl implements OrderConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(OrderConsumerServiceImpl.class);

    private static final String TX_PGROUP_NAME = "tx_order";
    @DubboReference
    private OrderService orderService;
    @DubboReference
    private OrderItemService orderItemService;
    @DubboReference
    private OrderShippingService orderShippingService;
    @DubboReference
    private ItemService itemService;
    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${custom.redis.item}")
    private String itemKey;

    @Value("${custom.redis.cart}")
    private String cartKey;

    @Value("${custom.cart.url}")
    private String cartUrl;

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    //新增
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addOrder(OrderParam orderParam, HttpServletRequest request) {

        // 发送消息
        Map<String,Object> param = new HashMap<>();
        param.put("orderParam",orderParam);
        param.put("request",request);

        List<TbOrderItem> orderItems = orderParam.getOrderItems();

        Message msg = MessageBuilder.withPayload(orderItems).
                setHeader(RocketMQHeaders.KEYS, "KEY_" + 2).build();
        SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(TX_PGROUP_NAME, "tx_order_msg" + ":" + "order", msg, param);
        logger.info("send Transactional msg body = {} , sendResult={}",msg.getPayload(),sendResult.getSendStatus());
        // 提前生成orderId
        String orderId = orderParam.getOrderId();
        orderParam.setOrderId(orderId);

        // 从缓存中获取结果
        BoundHashOperations orderInfo = redisTemplate.boundHashOps(orderId);
        Integer times = 0;
        while(orderInfo == null){
            times ++;
            if(times > 4){
                return null;
            }
            sleep();
        }
        String payment = (String)orderInfo.get("payment");
        String orderIdNew = (String)orderInfo.get("orderId");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,2);
        if(!orderIdNew.equals(orderId)){
            logger.error("出现异常应该获取的orderId不相同 orderId:{}  orderIdNew:{}",orderId,orderIdNew);
            return null;
        }
        Map result = new HashMap<>();
        result.put("orderId",orderId);
        result.put("payment",payment);
        result.put("date",c.getTime());
       return result;
    }

    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //新增
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrder(Map param) {
        OrderParam orderParam = (OrderParam)param.get("orderParam");
        HttpServletRequest request = (HttpServletRequest) param.get("request");
        Map<String, Object> map = new HashMap<>();
        //1.新增订单
        String orderId = orderParam.getOrderId();
        Date date = new Date();
        TbOrder tbOrder = new TbOrder();
        tbOrder.setOrderId(orderId);
        tbOrder.setCreateTime(date);
        tbOrder.setUpdateTime(date);
        tbOrder.setPayment(orderParam.getPayment());
        tbOrder.setPaymentType(orderParam.getPaymentType());
        tbOrder.setStatus(1);
        String token = CookieUtil.getCookieValue(request, "NX_TOKEN");
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
        TbUser tbUser = (TbUser) redisTemplate.opsForValue().get(token);
        tbOrder.setUserId(tbUser.getId());
        int index = 0;
        index += orderService.insertOrder(tbOrder);

        //2.
        List<TbOrderItem> orderItems = orderParam.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            orderItem.setId(IDUtil.nextId()+"");
            orderItem.setOrderId(orderId);
            index  += orderItemService.insertOrderItem(orderItem);
        }

        //3.新增订单发货
        TbOrderShipping orderShipping = orderParam.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        index += orderShippingService.insertOrderShipping(orderShipping);

        //mysql新增成功
        if(index ==orderItems.size()+2){
            //修改mysql及redis中商品的库存数量
            for (TbOrderItem orderItem : orderItems) {
                //根据id查询mysql中商品库存数量
                TbItem tbItem = itemService.selectItemById(Long.parseLong(orderItem.getItemId()));
                //计算剩余库存数量
                int retainNum = tbItem.getNum()-orderItem.getNum();
                //更新
                int result = itemService.updateNumById(Long.parseLong(orderItem.getItemId()), retainNum, date);
                if(result >0){
                    //mysql更新成功后修改redis中商品的库存数量
                    String finalItemKey = itemKey+orderItem.getItemId();
                    if(redisTemplate.hasKey(finalItemKey)){
                        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<SearchEntity>(SearchEntity.class));
                        SearchEntity se = (SearchEntity) redisTemplate.opsForValue().get(finalItemKey);
                        se.setNum(retainNum);
                        redisTemplate.opsForValue().set(finalItemKey,se);
                    }
                }
            }


            //删除redis中购物车对应的商品数据
            //该种方式在order中远程调用cart实现操作（传递参数当前下订单中的所有商品id,token）
            List<Long> ids = new ArrayList<>();
            for (TbOrderItem orderItem : orderItems) {
                ids.add(Long.parseLong(orderItem.getItemId()));
            }
            HttpClientUtil.doPostJson(cartUrl+token,JsonUtil.objectToJson(ids));





            //该种方式直接在order中操作cart
            /*String token = CookieUtil.getCookieValue(request, "NX_TOKEN");
            String finalCartKey="";
            if(Strings.isNotBlank(token)) {
                if (redisTemplate.hasKey(token)) {
                    //用户已登录系统
                    //从redis中获取用户数据
                    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
                    TbUser user = (TbUser) redisTemplate.opsForValue().get(token);
                    finalCartKey =  cartKey+ user.getId();
                }
            }

            List<SearchEntity> list =new ArrayList<>();
            for (TbOrderItem orderItem : orderItems) {
                redisTemplate.setValueSerializer(new StringRedisSerializer());
                String cartJson = (String) redisTemplate.opsForValue().get(finalCartKey);
                list = JsonUtil.jsonToList(cartJson, SearchEntity.class);
                List<SearchEntity> removeList = new ArrayList<>();
                for (SearchEntity se : list) {
                    if(orderItem.getItemId().equals(se.getId())){
                        removeList.add(se);
                    }
                }
                list.removeAll(removeList);
            }

            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<SearchEntity>(SearchEntity.class));
            redisTemplate.opsForValue().set(finalCartKey,list);*/

        }
     ;
        // 将获取的数据放到redis中
        BoundHashOperations orderInfo = redisTemplate.boundHashOps(orderId);
        orderInfo.put("orderId",orderId);
        orderInfo.put("payment",orderParam.getPayment());
    }


}
