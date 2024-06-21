package com.lanut.ordering_backend.configuration


import com.lanut.ordering_backend.entity.RestAuthFailure
import com.lanut.ordering_backend.entity.RestForbidden
import com.lanut.ordering_backend.entity.RestSuccess
import com.lanut.ordering_backend.entity.vo.VerifiedUser
import com.lanut.ordering_backend.filter.AuthLoginFilter
import com.lanut.ordering_backend.filter.JwtAuthorizeFilter
import com.lanut.ordering_backend.utils.JwtUtils
import jakarta.annotation.Resource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
open class SecurityConfiguration {

    @Resource
    lateinit var jwtUtils: JwtUtils

    @Resource
    lateinit var jwtAuthorizeFilter: JwtAuthorizeFilter

    @Resource
    lateinit var authLoginFilter: AuthLoginFilter

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { conf ->
            conf
                .requestMatchers("/api/auth/*", "/static/**", "/api/test/**").permitAll()
                .anyRequest().authenticated()
        }
            .formLogin { conf ->
                conf
                    .loginProcessingUrl("/api/auth/login").usernameParameter("openid")
                    .failureHandler { _, response, exception ->
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        response.writer.write(exception.message!!.RestAuthFailure().asJsonString())
                    }
                    .successHandler { request, response, authentication ->
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        val user = authentication.principal as VerifiedUser
                        val jwt = jwtUtils.createJwt(user)
                        val outputMessage = object {
                            val nickname = user.nickname
                            val role = user.role.authority
                            val openid = user.openid
                            val token = jwt
                        }
                        response.writer.write(outputMessage.RestSuccess().asJsonString())
                    }
            }
//            // 在微信小程序登录的过程中，无需进行登出操作
//            .logout { conf ->
//                conf
//                    .logoutUrl("/api/auth/logout")
//                    .logoutSuccessHandler { _, _, _ -> }
//            }
            .sessionManagement { conf ->
                conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling { conf ->
                conf
                    .authenticationEntryPoint { _, response, _ ->
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        response.writer.write("用户未登录".RestAuthFailure().asJsonString())
                    }
                    .accessDeniedHandler { _, response, accessDeniedException ->
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        response.writer.write("用户无权限: ${accessDeniedException.message}".RestForbidden().asJsonString())
                    }
            }
            .csrf { conf ->
                conf
                    .disable()
            }
            .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(authLoginFilter, jwtAuthorizeFilter::class.java)
            .build()
    }


}
