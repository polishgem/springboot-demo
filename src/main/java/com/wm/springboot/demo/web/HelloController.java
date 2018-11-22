package com.wm.springboot.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/hello")
    public String index() {
        logger.info("hello info");
        logger.error("hello error");
        return "Hello World2";
    }

}
