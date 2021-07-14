package com.nx.provider.service.impl;

import com.nx.api.service.TransactionLogService;
import com.nx.mapper.TbTransactionLogMapper;
import com.nx.pojo.TbTransactionLog;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author:   mkp
 * Date:     2020/12/22 22:12
 * Description:
 */
@DubboService
public class TransactionLogServiceImpl implements TransactionLogService {
    @Autowired
    private TbTransactionLogMapper transactionLogMapper;
    @Override
    public void addTransactionLog(TbTransactionLog transactionLog) {
        transactionLogMapper.insertSelective(transactionLog);
    }

    @Override
    public TbTransactionLog getById(String id) {
        return transactionLogMapper.selectByPrimaryKey(id);
    }
}
