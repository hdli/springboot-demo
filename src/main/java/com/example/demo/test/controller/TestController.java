package com.example.demo.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hdli on 2018-4-25.
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    /**
     * 测试全局异常处理，跳转指定错误页面
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/start")
    public String test1() throws Exception{
        throw  new Exception("发生错误");
    }

    @GetMapping(value = "/hello")
    public String hello(Model model) {
        Map map = new HashMap();
        map.put("name","Dear");
        map.put("url","https://www.baidu.com");
//        map.put("state","1");
//        model.addAttribute("name", "Dear");
        model.addAllAttributes(map);
        return "test/hello";
    }


}
