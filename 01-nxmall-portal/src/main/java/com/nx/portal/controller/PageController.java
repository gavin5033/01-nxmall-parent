package com.nx.portal.controller;

import com.nx.portal.service.PortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @Autowired
    private PortalService portalService;
    //显示首页index.jsp
    @RequestMapping("/")
    public String showIndex(Model model){
        String bigAd = portalService.showBigAd();
        model.addAttribute("ad1",bigAd);
        return "index";
    }
}
