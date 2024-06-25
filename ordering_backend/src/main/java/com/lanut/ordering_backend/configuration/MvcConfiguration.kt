package com.lanut.ordering_backend.configuration

import com.lanut.ordering_backend.filter.JwtTokenUserInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport


@Configuration
open class MvcConfiguration: WebMvcConfigurationSupport() {

    @Autowired
    lateinit var jwtTokenUserInterceptor: JwtTokenUserInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(jwtTokenUserInterceptor)
            .excludePathPatterns(
                "/static/**",
                "/api/test/**",
                "/api/common/**",
                "/api/dish/**",
                "/api/category/**",
                "/api/carousel/**",
                )
            .addPathPatterns("/**")
    }

}