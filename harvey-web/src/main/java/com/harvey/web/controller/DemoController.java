package com.harvey.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harvey
 * @date 2024-10-23 10:43
 **/
@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("")
    public String get() {

        return "demo get";
    }
}
