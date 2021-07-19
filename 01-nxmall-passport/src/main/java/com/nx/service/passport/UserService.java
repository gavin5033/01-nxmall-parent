package com.nx.service.passport;

import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    //根据客户端传过来的username和password查询数据库
    NxmallResult selectUserByUser(HttpServletRequest request, HttpServletResponse response, TbUser tbUser);

    //通过token查询用户信息
    NxmallResult selectUserByToken(String token);

    //安全退出
    NxmallResult logout(HttpServletRequest request, HttpServletResponse response, String token);

    //检查数据是否可用
    NxmallResult checkUser(String param, int type);

    //注册
    NxmallResult register(TbUser tbUser);
}
