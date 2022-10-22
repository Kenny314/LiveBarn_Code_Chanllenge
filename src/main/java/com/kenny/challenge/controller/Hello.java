package com.kenny.challenge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Hello {

    @RequestMapping(value = "hello")
    public String handle01(){
        return "Hello world!!";
    }
}
