package com.nx.interceptor;

import com.nx.pojo.NxmallResult;
import com.nx.utils.CookieUtil;
import com.nx.utils.HttpClientUtil;
import com.nx.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${custom.passport.token}")
    private String tokenUrl;

    @Value("${custom.passport.login}")
    private String loginUrl;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取当前请求的Cookie
        String cookieValue = CookieUtil.getCookieValue(request, "NX_TOKEN");
        //通过HttpClient向nxmal_passport发送请求
        String json = HttpClientUtil.doGet(tokenUrl + cookieValue);
        NxmallResult er = JsonUtil.jsonToPojo(json, NxmallResult.class);
        if(er!=null&&er.getStatus()==200){
            //登陆成功
            return  true;

        }
        response.sendRedirect(loginUrl);
        return false;
    }
}
