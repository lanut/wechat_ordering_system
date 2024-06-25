package com.lanut.ordering_backend.configuration


import com.lanut.ordering_backend.entity.vo.RestAuthFailure
import com.lanut.ordering_backend.entity.vo.RestForbidden
import com.lanut.ordering_backend.entity.vo.RestSuccess
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
                .requestMatchers(
                    "/api/auth/*",
                    "/static/**",
                    "/api/test/**",
                    "/api/common/**",
                    "/api/dish/**",
                    "/api/category/**",
                    "/api/carousel/**",
                ).permitAll() // 允许任何人访问的网站
                .requestMatchers("/api/admin/**").hasAnyRole("admin") // 仅允许管理员访问的网站
                .anyRequest().authenticated() // 其余需要验证用户可访问的网站
        }
            .formLogin { conf ->
                conf
                    .loginProcessingUrl("/api/auth/login").usernameParameter("openid")
                    .failureHandler { _, response, exception ->
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        response.writer.write(exception.message!!.RestAuthFailure())
                    }
                    .successHandler { _, response, authentication ->
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        val user = authentication.principal as VerifiedUser
                        val jwt = jwtUtils.createJwt(user)
                        @Suppress("unused")
                        val outputMessage = object {
                            val nickname = user.nickname
                            val role = user.role.authority
                            val openid = user.openid
                            val token = jwt
                        }
                        response.writer.write(outputMessage.RestSuccess())
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
                    .authenticationEntryPoint { _, response, authenticationException ->
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        response.writer.write("用户未登录: ${authenticationException.message}".RestAuthFailure())
                    }
                    .accessDeniedHandler { _, response, accessDeniedException ->
                        response.contentType = "application/json"
                        response.characterEncoding = "UTF-8"
                        response.writer.write("用户无权限: ${accessDeniedException.message}".RestForbidden())
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


