package com.lanut.ordering_backend.configuration


import com.lanut.ordering_backend.entity.RestFailure
import com.lanut.ordering_backend.entity.RestSuccess
import com.lanut.ordering_backend.entity.vo.AuthorizeVO
import com.lanut.ordering_backend.utils.JwtUtils
import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler

@Configuration
open class SecurityConfiguration {

    @Resource
    lateinit var jwtUtils: JwtUtils



    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { conf ->
            conf.requestMatchers("/api/auth/*").permitAll().anyRequest().authenticated()
        }.formLogin { conf ->
            conf.loginProcessingUrl("/api/auth/login").usernameParameter("username")
                .failureHandler(MyAuthenticationFailureHandler(jwtUtils)).successHandler(MyAuthenticationSuccessHandler(jwtUtils))
        }.logout { conf ->
            conf.logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(MyLogoutSuccessHandler(jwtUtils))
        }.sessionManagement{conf ->
            conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }.csrf{ conf ->
            conf.disable()
        }.build()
    }



}

class MyAuthenticationSuccessHandler(val jwtUtils: JwtUtils) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        val user = authentication.principal as User
        // TODO: 此处为测试数据，没有与数据库相连
        val token = jwtUtils.creatJwt(user, 1, "lanut")
        val authorizeVO = AuthorizeVO("lanut", "admin", token, jwtUtils.expireTime())
        response.writer.write(authorizeVO.RestSuccess().asJsonString())
    }
}

class MyAuthenticationFailureHandler(val jwtUtils: JwtUtils) : AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(exception.message!!.RestFailure(401).asJsonString())
    }
}

class MyLogoutSuccessHandler(val jwtUtils: JwtUtils) : LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
    }
}
