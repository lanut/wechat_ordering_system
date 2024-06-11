package com.lanut.ordering_backend.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Suppress("SameReturnValue")
@RestController
@RequestMapping("/api/test")
class TestController {
    @GetMapping("/hello")
    fun hello(): String {
        return "Hello, World!"
    }
}