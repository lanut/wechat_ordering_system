package com.lanut.ordering_backend.web

import com.lanut.ordering_backend.service.IUserService
import com.lanut.ordering_backend.utils.JwtTokenProvider
import jakarta.annotation.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    @Resource
    private val jwtTokenProvider: JwtTokenProvider,
    @Resource
    private val userService: IUserService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = userService.findByOpenid(loginRequest.openid)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")

        val token = jwtTokenProvider.createToken(user.openid!!, listOf(user.role!!))

        return ResponseEntity.ok(mapOf("token" to token))
    }
}

data class LoginRequest(val openid: String, val nickname: String)
