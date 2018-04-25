package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hdli on 2018-4-25.
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping(value = "/start")
    public String test1() throws Exception{
        throw  new Exception("发生错误");
    }
}
