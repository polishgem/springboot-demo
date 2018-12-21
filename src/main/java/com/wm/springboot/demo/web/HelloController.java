package com.wm.springboot.demo.web;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.wm.springboot.demo.domain.User;
import com.wm.springboot.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Value("${timeout}")
    private String timeout;

    @ApolloConfig
    private Config config;

    /**
     * get请求，登录页面跳转
     * @return
     */
    @GetMapping("/login")
    @ResponseBody
    public String login() {
        return null;
    }


    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        logger.info("hello info");
        logger.error("hello error");
        User user = userService.queryUser(1L);
        logger.info(user.getName());
        logger.info("config get timeout:"+config.getProperty("timeout", "0"));
        logger.info("timeout:"+timeout);
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
