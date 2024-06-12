package com.lanut.ordering_backend.web

import com.lanut.ordering_backend.entity.RestBean
import com.lanut.ordering_backend.entity.RestSuccess
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Suppress("SameReturnValue")
@RestController
@RequestMapping("/api/test")
class TestController {
    @GetMapping("/hello")
    fun hello(): String {
        return "Hello, World!".RestSuccess().asJsonString()
    }

    @GetMapping("/testFail")
    fun testFail(): String {
        return RestBean.failure(500).asJsonString()
    }
}