package common.niuniu.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.util.*

object JwtUtil {

    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims 设置的信息
     * @return 生成的JWT字符串
     */
    fun createJWT(secretKey: String, ttlMillis: Long, claims: Map<String, Any>?): String {
        val exp = Date(System.currentTimeMillis() + ttlMillis)
        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, secretKey.toByteArray(StandardCharsets.UTF_8))
            .setExpiration(exp)
            .compact()
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token 加密后的token
     * @return 解密后的Claims
     */
    fun parseJWT(secretKey: String, token: String): Claims {
        logInfo("校验token是否一致")
        return Jwts.parser()
            .setSigningKey(secretKey.toByteArray(StandardCharsets.UTF_8))
            .parseClaimsJws(token).body.also {
                logInfo("claims: $it")
            }
    }
}
