package common.niuniu.properties

import org.springframework.boot.context.properties.ConfigurationProperties

// 标志其为配置属性类，sky.jwt在yml里有标注，存储具体的key value
@ConfigurationProperties(prefix = "login-reg.jwt")
data class JwtProperties (
    /**
     * 管理端员工生成jwt令牌相关配置
     */
    val employeeSecretKey: String,
    val employeeTtl: Long = 0,
    val employeeTokenName: String,

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    val userSecretKey: String,
    val userTtl: Long,
    val userTokenName: String,
)
