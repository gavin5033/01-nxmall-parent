package com.nx.provider.service.impl;/*
 @Author SunJiuXiang
 @Package com.nx.provider.service.impl
 @CreateTime 2020/8/26
*/

import com.nx.api.service.UserService;
import com.nx.mapper.TbUserMapper;
import com.nx.pojo.TbUser;
import com.nx.pojo.TbUserExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    //根据客户端传过来的username和password查询数据库
    @Override
    public TbUser selectUserByUser(TbUser tbUser) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();

        if(Strings.isNotBlank(tbUser.getUsername())){
            criteria.andUsernameEqualTo(tbUser.getUsername());
        }
        if(Strings.isNotBlank(tbUser.getPassword())){
            criteria.andPasswordEqualTo(tbUser.getPassword());
        }
        if(Strings.isNotBlank(tbUser.getPhone())){
            criteria.andPhoneEqualTo(tbUser.getPhone());
        }
        if(Strings.isNotBlank(tbUser.getEmail())){
            criteria.andEmailEqualTo(tbUser.getEmail());
        }

        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
        if(tbUsers != null && tbUsers.size()>0){
            return tbUsers.get(0);
        }
        return null;
    }


    //新增
    @Override
    public int insertUser(TbUser tbUser) {

        return tbUserMapper.insertSelective(tbUser);
    }
}
