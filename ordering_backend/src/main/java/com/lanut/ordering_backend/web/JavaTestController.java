package com.lanut.ordering_backend.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JavaTestController {

    @ResponseBody
    @GetMapping("/Java")
    public String javaIndex() {
        return "Hello World Java";
    }
}
