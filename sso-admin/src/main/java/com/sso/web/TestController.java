package com.sso.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by wangmin on 2018/3/9.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("index")
    @RequiresRoles("admin")
    public String index(Model model, HttpSession session) {
        String username = (String) session.getAttribute("userName");
        model.addAttribute("userName", username);
//        model.addAttribute("token", SecurityUtils.getSubject().getSession().getId().toString());
        System.out.println(SecurityUtils.getSubject().getSession().getId().toString());
        return "index";
    }

    @RequestMapping("aaa")
    @ResponseBody
    public String aaa() {
        return "aaa";
    }

    @RequestMapping("bbb")
    @ResponseBody
    @RequiresRoles("manager")
    public String bbb() {
        return "bbb";
    }
}
