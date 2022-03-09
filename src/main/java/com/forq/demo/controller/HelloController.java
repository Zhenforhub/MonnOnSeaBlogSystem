package com.forq.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author long
 *
 * @date 2022/3/4
 * @apiNote
 */
@RestController

public class HelloController {

    @RequestMapping ("/hello")
    public String hello(){
        return "Hello world ! Welcom to visit waylua .com";
    }


}
