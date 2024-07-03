package common.niuniu.server

import common.niuniu.properties.JwtProperties
import common.niuniu.properties.WeChatProperties
import common.niuniu.utils.logInfo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ServerApplicationTests {


    @Autowired
    private lateinit var jwtProperties: JwtProperties

    @Autowired
    private lateinit var weChatProperties: WeChatProperties

    @Test
    fun contextLoads() {
        logInfo(jwtProperties.toString())
        logInfo(weChatProperties.toString())
    }

}
