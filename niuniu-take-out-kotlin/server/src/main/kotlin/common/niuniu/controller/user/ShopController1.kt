package common.niuniu.controller.user

import common.niuniu.annotation.Slf4j
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.result.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("userShopController") // 重命名，防止两个端admin、user的ShopController冲突
@RequestMapping("/user/shop")
@Slf4j
class ShopController {
    @Autowired
    private val redisTemplate: RedisTemplate<*, Any>? = null

    @get:GetMapping("/status")
    val status: Result<Int>
        get() {
            val status = redisTemplate!!.opsForValue()[KEY] as Int
            log.info("当前店铺状态为：{}", if (status == 1) "营业中" else "打烊中")
            return Result.success(status)
        }

    companion object {
        const val KEY: String = "SHOP_STATUS"
    }
}
