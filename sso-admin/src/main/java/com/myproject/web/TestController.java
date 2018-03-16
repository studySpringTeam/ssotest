package com.myproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wangmin on 2018/3/9.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("userName", "王敏");
        return "index";
    }
}
