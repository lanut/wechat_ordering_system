package com.lanut.ordering_backend.filter

import com.lanut.ordering_backend.context.BaseContext
import com.lanut.ordering_backend.utils.JwtUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor


@Component
class JwtTokenUserInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var utils: JwtUtils


    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (handler !is HandlerMethod) {
            //当前拦截到的不是动态方法，直接放行
            return true
        }

        //1、从请求头中获取令牌
        val token = request.getHeader("Authorization")

        try {
            //2、校验令牌
            val userDetail = utils.tokenToUserDetail(token)
            val openid = userDetail.openid
            BaseContext.currentOpenid = openid
            request.setAttribute("openid", openid)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}
