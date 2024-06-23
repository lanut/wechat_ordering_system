package com.lanut.ordering_backend.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI


@Component
class AuthLoginFilter : OncePerRequestFilter() {
    private var requiresAuthenticationRequestMatcher: RequestMatcher =
        RequestMatcher { request -> // 匹配 `/api/auth/login` 请求
            request?.let { AntPathRequestMatcher("/api/auth/login").matches(it) } ?: false
        }// 请求匹配器


    @Value("\${wechat.appid}")
    private var appId: String = "" // 小程序id
    @Value("\${wechat.secret}")
    private var secret: String = "" // 小程序秘钥

    @Suppress("PrivatePropertyName")
    private val WX_URL: String = "https://api.weixin.qq.com/sns/jscode2session" // 微信登录url


    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 拦截微信登录
        if (requiresAuthenticationRequestMatcher.matches(request)) {
/*
            //todo 从request中获取 code 参数 这里逻辑根据你的情况自行实现
            val jsCode = "你自行实现从request中获取"
            //todo 必要的校验自己写
            val queryParams: MultiValueMap<String, String?> = LinkedMultiValueMap()
            queryParams.add("appid", this.appId)
            queryParams.add("secret", this.secret)
            queryParams.add("js_code", jsCode)
            queryParams.add("grant_type", "authorization_code")


            val uri: URI = UriComponentsBuilder.fromHttpUrl(WX_URL)
                .queryParams(queryParams)
                .build()
                .toUri()
            //todo 这里 Object 自行封装为具体对象
            val result: ResponseEntity<Any> = restTemplate.getForEntity(uri, Any::class.java)

            //todo 处理 result 比如后端存储、后端授权、角色资源处理、注册、对session_key的处理等等你需要的业务逻辑
            // 最后放入HttpServletResponse中返回前端返回
*/
            filterChain.doFilter(request, response)
        } else {
            filterChain.doFilter(request, response)
        }
    }
}