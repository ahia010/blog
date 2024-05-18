package com.ahia.blog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class MainController {
    @GetMapping("/")
    public String index(){
        return "index";
    }

}
