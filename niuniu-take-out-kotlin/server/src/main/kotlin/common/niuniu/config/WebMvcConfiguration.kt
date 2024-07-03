package common.niuniu.config

import common.niuniu.interceptor.JwtTokenAdminInterceptor
import common.niuniu.interceptor.JwtTokenUserInterceptor
import common.niuniu.json.JacksonObjectMapper
import common.niuniu.utils.logInfo
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
@Slf4j
class WebMvcConfiguration : WebMvcConfigurationSupport() {
    @Autowired
    private val jwtTokenAdminInterceptor: JwtTokenAdminInterceptor? = null

    @Autowired
    private val jwtTokenUserInterceptor: JwtTokenUserInterceptor? = null

    /**
     * 配置，添加自定义拦截器
     * @param registry
     */
    public override fun addInterceptors(registry: InterceptorRegistry) {
        logInfo("自定义好了拦截器，还要在这个WebMvc配置类里注册")
        registry.addInterceptor(jwtTokenAdminInterceptor!!)
            .addPathPatterns("/admin/**")
            .excludePathPatterns("/admin/employee/login")
            .excludePathPatterns("/admin/employee/register")
        registry.addInterceptor(jwtTokenUserInterceptor!!)
            .addPathPatterns("/user/**")
            .excludePathPatterns("/user/user/login")
            .excludePathPatterns("/user/shop/status")
    }

    /**
     * 扩展Spring MVC框架的消息转化器，用于格式化时间等
     * @param converters
     */
    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        logInfo("扩展消息转换器...")
        //创建一个消息转换器对象
        val converter = MappingJackson2HttpMessageConverter()
        //需要为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
        converter.objectMapper = JacksonObjectMapper()
        //将自己的消息转化器加入容器中
        converters.add(0, converter)
    }
}
