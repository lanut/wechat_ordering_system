package com.lanut.ordering_backend.configuration


import com.lanut.ordering_backend.entity.RestFailure
import com.lanut.ordering_backend.entity.RestSuccess
import com.lanut.ordering_backend.entity.vo.AuthorizeVO
import com.lanut.ordering_backend.filter.JwtAuthorizeFilter
import com.lanut.ordering_backend.utils.JwtUtils
import jakarta.annotation.Resource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
open class SecurityConfiguration {

    @Resource
    lateinit var jwtUtils: JwtUtils

    @Resource
    lateinit var jwtAuthorizeFilter: JwtAuthorizeFilter

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { conf -> conf
            .requestMatchers("/api/auth/*", "/static/**").permitAll()
            .anyRequest().authenticated()
        }
        .formLogin { conf -> conf
            .loginProcessingUrl("/api/auth/login").usernameParameter("username")
            .failureHandler { _, response, exception ->
                response.contentType = "application/json"
                response.characterEncoding = "UTF-8"
                response.writer.write(exception.message!!.RestFailure(401).asJsonString())
            }
            .successHandler { request, response, authentication ->
                response.contentType = "application/json"
                response.characterEncoding = "UTF-8"
                val user = authentication.principal as User
                // TODO: 此处为测试数据，没有与数据库相连
                val token = jwtUtils.creatJwt(user, 1, "lanut")
                val authorizeVO = AuthorizeVO("lanut", "admin", token, jwtUtils.expireTime())
                response.writer.write(authorizeVO.RestSuccess().asJsonString())
            }
        }
        .logout { conf -> conf
            .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler { _, _, _ ->

                }
        }
        .sessionManagement { conf -> conf
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
            .exceptionHandling { conf -> conf
                .authenticationEntryPoint { _, response, _ ->
                    response.contentType = "application/json"
                    response.characterEncoding = "UTF-8"
                    response.writer.write("用户未登录".RestFailure(401).asJsonString())
                }
            }
        .csrf { conf -> conf
            .disable()
        }
        .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()
    }




}
