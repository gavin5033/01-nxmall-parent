package com.nx.transaction;

import com.nx.api.service.TransactionLogService;
import com.nx.pojo.OrderParam;
import com.nx.pojo.TbTransactionLog;
import com.nx.service.OrderConsumerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@RocketMQTransactionListener(txProducerGroup = "tx_order")
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderConsumerService orderConsumerService;
    @DubboReference
    private TransactionLogService transactionLogService;
    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<String, Integer>();

    /**
     * 如果半消息发送成功，那么执行本地事务=业务逻辑+事务记录
     * @param msg
     * @param arg
     * @return
     */
       @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
         String transId = (String)msg.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID);
        Map<String,Object> param = (Map)arg;
        OrderParam orderParam = (OrderParam)param.get("orderParam");
        TbTransactionLog transactionLog = new TbTransactionLog();
        transactionLog.setId(transId);
        transactionLog.setBusiness("order");
        transactionLog.setCreated(new Date());
        transactionLog.setUpdated(new Date());
        transactionLog.setForeignKey(orderParam.getOrderId());
        try{
            orderConsumerService.addOrder(param);
            transactionLogService.addTransactionLog(transactionLog);
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e){
            return RocketMQLocalTransactionState.ROLLBACK;
        }

    }


    /**
     * 回查接口
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String transId = (String)msg.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID);
        TbTransactionLog transactionLog = transactionLogService.getById(transId);
        if(transactionLog != null){
            return RocketMQLocalTransactionState.COMMIT;
        }else{
            return RocketMQLocalTransactionState.UNKNOWN;
        }

    }
}