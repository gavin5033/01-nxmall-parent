package com.nx.service.passport.impl;

import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbUser;
import com.nx.service.passport.UserService;
import com.nx.utils.CookieUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @DubboReference
    private com.nx.api.service.UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    //根据客户端传过来的username和password查询数据库
    @Override
    public NxmallResult selectUserByUser(HttpServletRequest request, HttpServletResponse response, TbUser tbUser) {
        NxmallResult er = new NxmallResult();
        String password = tbUser.getPassword();
        System.out.println("页面原始密码："+password);
        System.out.println("用户名："+tbUser.getUsername());
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println("加密之后密码："+md5Password);
        tbUser.setPassword(md5Password);
        TbUser user = userService.selectUserByUser(tbUser);
        if(user != null){
            user.setPassword("");
            //生成随机cookie值
            String cookieValue = UUID.randomUUID().toString();
            //给Cookie赋值
            CookieUtil.setCookie(request,response,"NX_TOKEN",cookieValue,604800);
            //给redis赋值
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
            redisTemplate.opsForValue().set(cookieValue, user);

            er.setStatus(200);
            er.setMsg("OK");
            er.setData(cookieValue);
        }else{
            er.setMsg("登录失败！");
        }
        return er;
    }


    //通过token查询用户信息
    @Override
    public NxmallResult selectUserByToken(String token) {
        NxmallResult er = new NxmallResult();
        //从redis中获取用户数据
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<TbUser>(TbUser.class));
        TbUser user = (TbUser) redisTemplate.opsForValue().get(token);
        if(user != null){
            er.setStatus(200);
            er.setMsg("OK");
            er.setData(user);
        }else{
            er.setMsg("用户不存在");
        }
        return er;
    }


    //安全退出
    @Override
    public NxmallResult logout(HttpServletRequest request, HttpServletResponse response, String token) {
        NxmallResult er = new NxmallResult();
        //1.清空对应的Cookie
        CookieUtil.deleteCookie(request, response, token);
        //2.清空redis对应的用户数据
        Boolean result = redisTemplate.delete(token);
        if(result){
            er.setStatus(200);
            er.setMsg("OK");
            er.setData("");
        }
        return er;
    }


    //检查数据是否可用
    @Override
    public NxmallResult checkUser(String param, int type) {
        NxmallResult er = new NxmallResult();
        TbUser tbUser = new TbUser();
        if(type == 1){
            tbUser.setUsername(param);
        }else if(type == 2){
            tbUser.setPhone(param);
        }else{
            tbUser.setEmail(param);
        }

        TbUser user = userService.selectUserByUser(tbUser);
        if(user != null){
            er.setData(false);
        }else{
            er.setStatus(200);
            er.setMsg("OK");
            er.setData(true);
        }
        return er;
    }


    //注册
    @Override
    public NxmallResult register(TbUser tbUser) {
        NxmallResult er = new NxmallResult();
        Date date = new Date();
        tbUser.setUpdated(date);
        tbUser.setCreated(date);

        String password = tbUser.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        tbUser.setPassword(md5Password);

        int index = userService.insertUser(tbUser);
        if(index >0){
            er.setStatus(200);
            er.setMsg("OK");
            er.setData("");
        }else{
            er.setStatus(400);
            er.setMsg("注册失败. 请校验数据后请再提交数据.");
            er.setData(null);
        }
        return er;
    }
}
