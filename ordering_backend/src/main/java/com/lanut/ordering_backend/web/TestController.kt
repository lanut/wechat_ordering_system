package com.lanut.ordering_backend.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class TestController {
    @ResponseBody
    @GetMapping("/")
    fun index(): String {
        return "Hello World Kotlin"
    }
}