package common.niuniu.interceptor

import common.niuniu.constant.JwtClaimsConstant
import common.niuniu.context.BaseContext.currentId
import common.niuniu.properties.JwtProperties
import common.niuniu.utils.JwtUtil.parseJWT
import common.niuniu.utils.logInfo
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
class JwtTokenAdminInterceptor : HandlerInterceptor {
    @Autowired
    private lateinit var jwtProperties: JwtProperties

    /**
     * 对controller资源进行请求之前，需要校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (handler !is HandlerMethod) {
            // 当前拦截到的不是Controller动态方法，直接放行
            return true
        }
        // 1、从请求头中获取令牌
        val token = request.getHeader(jwtProperties.employeeTokenName)
        println("-------------------------------- token -------------------------------- $token")
        // 2、校验令牌
        try {
            logInfo("jwt校验:$token")
            val claims = parseJWT(jwtProperties.employeeSecretKey, token)
            val employeeId = claims[JwtClaimsConstant.EMPLOYEE_ID].toString().toInt()
            logInfo("当前用户id：$employeeId")
            // 将id存到当前线程thread的局部空间里面，并在controller,service或者其他地方进行调用获取id
            currentId = employeeId
            // 3、通过，放行
            return true
        } catch (ex: Exception) {
            // 4、不通过，响应`401`状态码
            response.status = 401
            return false
        }
    }
}
