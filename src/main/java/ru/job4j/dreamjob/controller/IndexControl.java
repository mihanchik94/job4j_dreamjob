package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexControl {
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/example1")
    public String example1() {
        return "example1";
    }

    @GetMapping("/example2")
    public String example2() {
        return "example2";
    }

    @GetMapping("/example3")
    public String example3() {
        return "example3";
    }

    @GetMapping("/example4")
    public String example4() {
        return "example4";
    }

    @GetMapping("/example5")
    public String example5() {
        return "example5";
    }
}