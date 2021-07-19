package com.nx.controller;

import com.nx.pojo.NxmallResult;
import com.nx.pojo.TbUser;
import com.nx.service.passport.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    //显示登录页面
    @RequestMapping("/user/showLogin")
    public String showLoginPage(@RequestHeader(name = "Referer",defaultValue = "")String url,  Model model){
        if(url.endsWith("/user/showRegister")){
            url = "";
        }
        model.addAttribute("redirect",url);
        return "login";
    }

    //登录
    @RequestMapping("/user/login")
    @ResponseBody
    public NxmallResult login(HttpServletRequest request, HttpServletResponse response, TbUser tbUser){
        return userService.selectUserByUser(request, response, tbUser);
    }
    //通过token查询用户信息
    @RequestMapping("/user/token/{token}")
    //@GetMapping
    @ResponseBody
    @CrossOrigin
    public NxmallResult selectUserByToken(@PathVariable String token){

        return userService.selectUserByToken(token);
    }
    //安全退出
    //@RequestMapping(value="/user/logout/{token}",method=RequestMethod.Get)
    //@RequestMapping("/user/logout/{token}")
    @PostMapping("/user/logout/{token}")
    @ResponseBody
    @CrossOrigin
    public NxmallResult logout(HttpServletRequest request, HttpServletResponse response, @PathVariable String token){
        return userService.logout(request, response, token);
    }

    //显示注册页面
    @RequestMapping("/user/showRegister")
    public String showRegister(){
        return "register";
    }

    //检查数据是否可用
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    @CrossOrigin
    public NxmallResult checkUser(@PathVariable String param, @PathVariable int type){
        return userService.checkUser(param, type);
    }

    //注册
    @RequestMapping("/user/register")
    @ResponseBody
    public NxmallResult register(TbUser tbUser){
        return userService.register(tbUser);
    }


}
