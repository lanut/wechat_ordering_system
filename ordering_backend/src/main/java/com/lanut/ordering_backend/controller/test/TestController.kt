package com.lanut.ordering_backend.controller.test

import com.lanut.ordering_backend.entity.RestSuccess
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController {

    @RequestMapping("/hello")
    fun hello(): String {
        return "Hello World!".RestSuccess().asJsonString()
    }

}

@RestController
@RequestMapping("/api/authTest")
class AuthTestController {

    @RequestMapping("/hello")
    fun hello(): String {
        return "Hello World!".RestSuccess().asJsonString()
    }

}