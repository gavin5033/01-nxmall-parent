package com.nx.api.service;

import com.nx.pojo.TbTransactionLog;

public interface TransactionLogService {
    void addTransactionLog(TbTransactionLog transactionLog);

    TbTransactionLog getById(String id);
}
