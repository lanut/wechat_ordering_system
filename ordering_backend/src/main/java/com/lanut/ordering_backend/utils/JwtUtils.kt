package com.lanut.ordering_backend.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import com.lanut.ordering_backend.entity.vo.VerifiedUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {
    @Value("\${spring.security.jwt.key}")
    val jwtSecret: String = "" // 指定秘钥，此原理为body明文的信息加密校验到第三部分来验证jwt是否被篡改，无法对body部分加密

    @Value("\${spring.security.jwt.expire}")
    val jwtExpireTime: Int = 0

    private val sign:Algorithm
        get() = Algorithm.HMAC256(jwtSecret)


    private fun parseJwt(token: String): Map<String, Claim> {
        val jwtVerifier = JWT.require(sign).build()
        val claims = try {
            jwtVerifier.verify(token).claims
        } catch (e: JWTVerificationException) {
            throw e
        }
        return claims
    }

    // 处理标头
    private fun tokenParseToJwtToken(token: String): String {
        // 验证并去掉Token的 `Bearer `前缀并返回，否则抛出异常
        if (token.startsWith("Bearer ")) {
            return token.substring(7)
        } else {
            throw IllegalArgumentException("Token格式不正确")
        }
    }

    fun tokenToUserDetail(token: String): VerifiedUser {
        // 先处理标头
        val jwtToken = tokenParseToJwtToken(token)
        // 解析Token
        val claims = parseJwt(jwtToken)
        // 获取载荷
        val nickName: String = claims["nickname"]!!.asString()
        val openid: String = claims["openid"]!!.asString()
        val role: String = claims["role"]!!.asString()
        return VerifiedUser(nickName, openid, role)
    }

    fun createJwt(user: VerifiedUser): String {
        // 设置过期时间
        val expireTime = Calendar.getInstance().apply {
            add(Calendar.SECOND, jwtExpireTime)
        }.time
        // 设置body载荷，此处为明文传输
        val token = JWT.create()
            // 添加jwt的信息（注意：此消息为明文存储）
            .withClaim("nickname", user.nickname)
            .withClaim("openid", user.openid)
            .withClaim("role", user.role.getName())
            .withExpiresAt(expireTime)
            .withIssuedAt(Date())
            .sign(sign)
        return token
    }

}

