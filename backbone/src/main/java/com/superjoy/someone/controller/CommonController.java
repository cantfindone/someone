package com.superjoy.someone.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class CommonController {

    @GetMapping(value = "/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("/index");
        return mav;
    }

}
