package com.simplespringbootproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by wangmin on 2018/3/17.
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public String index(Model model, HttpSession session) {
        String username = (String) session.getAttribute("userName");
        model.addAttribute("userName", "王敏");
        return "index";
    }
}
