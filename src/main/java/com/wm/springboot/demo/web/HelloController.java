package com.wm.springboot.demo.web;

import com.wm.springboot.demo.domain.User;
import com.wm.springboot.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        logger.info("hello info");
        logger.error("hello error");
        User user = userService.queryUser(1L);
        logger.info(user.getName());
        return "Hello World2";
    }

    @RequestMapping("/")
    public String index(ModelMap map) {
        map.addAttribute("test", "test");
        return "index";
    }

    @ResponseBody
    @RequestMapping("/admin/config")
    public String adminConfig() {
        return "admin";
    }
}
