package com.lanut.ordering_backend.configuration


import com.lanut.ordering_backend.entity.RestAuthFailure
import com.lanut.ordering_backend.entity.RestForbidden
import com.lanut.ordering_backend.service.CustomOAuth2UserService
import com.lanut.ordering_backend.service.IUserService
import com.lanut.ordering_backend.utils.JwtTokenProvider
import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@Configuration
open class SecurityConfiguration {
    @Resource
    lateinit var jwtTokenProvider: JwtTokenProvider
    @Resource
    lateinit var clientRegistrationRepository: ClientRegistrationRepository

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { conf -> conf
            .requestMatchers("/api/auth/*", "/static/**").permitAll()
            .anyRequest().authenticated()
        }
        .oauth2Login { conf -> conf
            .clientRegistrationRepository(clientRegistrationRepository)
            .loginPage("/api/auth/login")
            .userInfoEndpoint { conf -> conf
                .userService(customOAuth2UserService())
            }
            .successHandler(oAuth2AuthenticationSuccessHandler())
        }
        .sessionManagement { conf -> conf
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .exceptionHandling { conf -> conf
            .authenticationEntryPoint { _, response, _ ->
                response.contentType = "application/json"
                response.characterEncoding = "UTF-8"
                response.writer.write("用户未登录".RestAuthFailure().asJsonString())
            }
            .accessDeniedHandler { request, response, accessDeniedException ->
                response.contentType = "application/json"
                response.characterEncoding = "UTF-8"
                response.writer.write("用户无权限".RestForbidden().asJsonString())
            }
        }
        .csrf { conf -> conf
            .disable()
        }
        .build()
    }

    @Bean
    open fun customOAuth2UserService(): CustomOAuth2UserService {
        return CustomOAuth2UserService()
    }

    @Bean
    open fun oAuth2AuthenticationSuccessHandler(): OAuth2AuthenticationSuccessHandler {
        return OAuth2AuthenticationSuccessHandler(jwtTokenProvider)
    }
}

class OAuth2AuthenticationSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider
) : AuthenticationSuccessHandler {
    @Resource
    lateinit var userService: IUserService

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as DefaultOAuth2User
        val openid = oAuth2User.name
        val user = userService.findByOpenid(openid)
        val token = jwtTokenProvider.createToken(user!!.openid!!, listOf(user.role!!))

        response.addHeader("Authorization", "Bearer $token")
    }
}

