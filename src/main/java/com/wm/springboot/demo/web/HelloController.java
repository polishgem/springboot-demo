package com.wm.springboot.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        logger.info("hello info");
        logger.error("hello error");
        return "Hello World2";
    }

    @RequestMapping("/")
    public String index(ModelMap map) {
        map.addAttribute("test", "test");
        return "index";
    }

}
