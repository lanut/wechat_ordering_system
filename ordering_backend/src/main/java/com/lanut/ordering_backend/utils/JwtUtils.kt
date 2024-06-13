package com.lanut.ordering_backend.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Calendar
import java.util.Date

@Component
class JwtUtils {

    @Value("\${spring.security.jwt.key}")
    val key = ""

    @Value("\${spring.security.jwt.expire}")
    val expire = 0

    fun expireTime(): Date {
        val instance = Calendar.getInstance()
        instance.add(Calendar.SECOND, expire)
        return instance.time
    }


    fun creatJwt(userDetails: UserDetails, id: Int, username: String): String {
        return JWT.create()
            .withClaim("id", id)
            .withClaim("name", username)
            .withClaim("authorities", userDetails.authorities.map(GrantedAuthority::getAuthority).toList())
            .withExpiresAt(expireTime())
            .withIssuedAt(Date())
            .sign(Algorithm.HMAC256(key))
    }

    fun conventToken(headerToken: String): String? {
        if (!headerToken.startsWith("Bearer ")) {
            return null
        }
        return headerToken.substring(7)
    }

    fun resolveJwt(headerToken: String): DecodedJWT? {
        val token = this.conventToken(headerToken)
        if (token == null) {
            return null
        }
        try {
            val jwtVerifier = JWT.require(Algorithm.HMAC256(key)).build()
            val verify = jwtVerifier.verify(token)
            val expiresAt = verify.expiresAt
            return if (Date().before(expiresAt)) verify else null
        } catch (_: JWTVerificationException) {
            return null
        }
    }

    fun toUser(jwt: DecodedJWT): UserDetails {
        val claims = jwt.claims
        return User
            .withUsername(claims["name"]!!.asString())
            .password("********")
            .authorities(*(claims["authorities"]?.asArray(String::class.java)))
            .build()
    }

    fun toId(jWT: DecodedJWT): Int {
        return jWT.claims["id"]!!.asInt()
    }
}
