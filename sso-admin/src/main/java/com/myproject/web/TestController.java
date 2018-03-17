package com.myproject.web;

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
    public String index(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("userName", username);
        return "index";
    }

    @RequestMapping("aaa")
    @ResponseBody
    public String aaa() {
        return "aaa";
    }

    @RequestMapping("bbb")
    @ResponseBody
    public String bbb() {
        return "bbb";
    }
}
