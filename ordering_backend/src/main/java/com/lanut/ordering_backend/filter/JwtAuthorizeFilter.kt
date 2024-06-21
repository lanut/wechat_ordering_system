package com.lanut.ordering_backend.filter

import com.lanut.ordering_backend.utils.JwtUtils
import jakarta.annotation.Resource
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Slf4j
@Component
class JwtAuthorizeFilter : OncePerRequestFilter() {

    @Resource
    lateinit var utils: JwtUtils


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 获取Token请求头，如果没有就下一步验证
        val authorization = request.getHeader("Authorization")
        if (authorization == null) {
            filterChain.doFilter(request, response)
            return
        }
        // 处理jwt Token 转为UserDetail
        val user = try {
            utils.tokenToUserDetail(authorization)
        } catch (e: Exception) {
            logger.error("Token解析失败: ${e.message}")
            filterChain.doFilter(request, response)
            return
        }
        // 把 UserDetail 转为 下一个过滤器UsernamePassword验证的方式(为了过下一个请求)
        val authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
        authentication.details = (WebAuthenticationDetailsSource().buildDetails(request))
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}