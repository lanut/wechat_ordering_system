package com.lanut.ordering_backend



import com.lanut.ordering_backend.entity.vo.RestSuccess
import com.lanut.ordering_backend.entity.vo.VerifiedUser
import com.lanut.ordering_backend.service.IUserService
import com.lanut.ordering_backend.utils.JwtUtils
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class CommonTest {
    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Value("\${spring.security.jwt.key}")
    private val jwtSecret: String = "" // 指定秘钥，此原理为body明文的信息加密校验到第三部分来验证jwt是否被篡改，无法对body部分加密

    @Value("\${spring.security.jwt.expire}")
    private val jwtExpireTime: Int = 0


    private val logger = LoggerFactory.getLogger(CommonTest::class.java)

    @Test
    fun test() {
        println(jwtSecret)
        println(jwtExpireTime)
    }


    @Test
    fun jwtUtilsTest() {
        val user = VerifiedUser("nickname", "openid", "admin")
        val jws = jwtUtils.createJwt(user)
        logger.info(jws)
        val userDetail = jwtUtils.tokenToUserDetail("Bearer $jws")
        logger.info(userDetail.username)
        logger.info(userDetail.authorities.map {
            it.authority
        }.toString())
        logger.info(userDetail.toString())
        try {
            jwtUtils.tokenToUserDetail(jws)
        } catch (e: Exception) {
            logger.info(e.message)
        }
    }

    @Value("\${spring.web.resources.carousel-path}")
    val path = ""

    @Resource
    lateinit var userService: IUserService

    @Test
    fun getUserOrdersTest() {
//        val userOrders = userService.getUserOrders("wx1234567890")
//        println(userOrders.RestSuccess())
    }

    @Test
    fun uploadPathTest() {
        println("$path/text1.txt")
        val file = java.io.File("$path/text1.txt")
        println(file.exists())
    }


}

