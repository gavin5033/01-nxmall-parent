package com.nx.mapper;

import com.nx.pojo.TbTransactionLog;
import com.nx.pojo.TbTransactionLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbTransactionLogMapper {
    long countByExample(TbTransactionLogExample example);

    int deleteByExample(TbTransactionLogExample example);

    int deleteByPrimaryKey(String id);

    int insert(TbTransactionLog record);

    int insertSelective(TbTransactionLog record);

    List<TbTransactionLog> selectByExample(TbTransactionLogExample example);

    TbTransactionLog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TbTransactionLog record, @Param("example") TbTransactionLogExample example);

    int updateByExample(@Param("record") TbTransactionLog record, @Param("example") TbTransactionLogExample example);

    int updateByPrimaryKeySelective(TbTransactionLog record);

    int updateByPrimaryKey(TbTransactionLog record);
}