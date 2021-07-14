package com.nx.api.service;

import com.nx.pojo.TbUser;

public interface UserService {
    //根据客户端传过来的username和password查询数据库
    TbUser selectUserByUser(TbUser tbUser);

    //新增
    int insertUser(TbUser tbUser);
}
