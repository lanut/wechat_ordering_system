package com.lanut.ordering_backend.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Component
import java.util.Calendar
import java.util.Date

//@Component
//class JwtUtils {
//
//    @Value("\${spring.security.jwt.key}")
//    val key = ""
//
//    @Value("\${spring.security.jwt.expire}")
//    val expire = 0
//
//    fun expireTime(): Date {
//        val instance = Calendar.getInstance()
//        instance.add(Calendar.SECOND, expire)
//        return instance.time
//    }
//
//
//    fun creatJwt(userDetails: UserDetails, id: Int, username: String): String {
//        return JWT.create()
//            .withClaim("id", id)
//            .withClaim("name", username)
//            .withClaim("authorities", userDetails.authorities.map(GrantedAuthority::getAuthority).toList())
//            .withExpiresAt(expireTime())
//            .withIssuedAt(Date())
//            .sign(Algorithm.HMAC256(key))
//    }
//
//    fun conventToken(headerToken: String): String? {
//        if (!headerToken.startsWith("Bearer ")) {
//            return null
//        }
//        return headerToken.substring(7)
//    }
//
//    fun resolveJwt(headerToken: String): DecodedJWT? {
//        val token = this.conventToken(headerToken)
//        if (token == null) {
//            return null
//        }
//        try {
//            val jwtVerifier = JWT.require(Algorithm.HMAC256(key)).build()
//            val verify = jwtVerifier.verify(token)
//            val expiresAt = verify.expiresAt
//            return if (Date().before(expiresAt)) verify else null
//        } catch (_: JWTVerificationException) {
//            return null
//        }
//    }
//
//    fun toUser(jwt: DecodedJWT): UserDetails {
//        val claims = jwt.claims
//        return User
//            .withUsername(claims["name"]!!.asString())
//            .password("********")
//            .authorities(*(claims["authorities"]?.asArray(String::class.java)))
//            .build()
//    }
//
//    fun toId(jWT: DecodedJWT): Int {
//        return jWT.claims["id"]!!.asInt()
//    }
//}

@Component
class JwtTokenProvider(
    @Resource
    private val userDetailsService: UserDetailsService
) {

    @Value("\${spring.security.jwt.key}")
    private val secretKey = "secret-key"

    @Value("\${spring.security.jwt.expire}")
    val expire = 0

    fun expireTime(): Date {
        val instance = Calendar.getInstance()
        instance.add(Calendar.SECOND, expire)
        return instance.time
    }

    fun createToken(username: String, roles: List<String>): String {
        val claims = Jwts.claims().setSubject(username)
        claims["roles"] = roles

        val now = Date()
        val validity = expireTime()

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUsername(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun validateToken(token: String): Boolean {
        try {
            val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return !claims.body.expiration.before(Date())
        } catch (e: JwtException) {
            throw JwtException("Expired or invalid JWT token")
        } catch (e: IllegalArgumentException) {
            throw JwtException("Expired or invalid JWT token")
        }
    }
}
