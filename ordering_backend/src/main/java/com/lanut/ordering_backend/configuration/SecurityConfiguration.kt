package com.lanut.ordering_backend.configuration


import com.lanut.ordering_backend.entity.RestFailure
import com.lanut.ordering_backend.entity.RestSuccess
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler

@Configuration
open class SecurityConfiguration {

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { conf ->
            conf.requestMatchers("/api/auth/*").permitAll().anyRequest().authenticated()
        }.formLogin { conf ->
            conf.loginProcessingUrl("/api/auth/login").usernameParameter("username")
                .failureHandler(MyAuthenticationFailureHandler()).successHandler(MyAuthenticationSuccessHandler())
        }.logout { conf ->
            conf.logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(MyLogoutSuccessHandler())
        }.sessionManagement{conf ->
            conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }.csrf{ conf ->
            conf.disable()
        }.build()
    }
}

class MyAuthenticationSuccessHandler:AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("登录成功".RestSuccess().asJsonString())
    }
}

class MyAuthenticationFailureHandler: AuthenticationFailureHandler {
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
class MyLogoutSuccessHandler: LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

    }

}
