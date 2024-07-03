package common.niuniu.controller.admin

import common.niuniu.annotation.Slf4j
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.result.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.*

// 注意：管理端能查看所有订单信息，而用户端只能根据userId查询到自己的历史订单信息
@RestController("adminShopController") // 重命名，防止两个端admin、user的ShopController冲突
@RequestMapping("/admin/shop")
@Slf4j
class ShopController {
    @Autowired
    private val redisTemplate: RedisTemplate<String, Any>? = null

    @PutMapping("/{status}")
    fun setStatus(@PathVariable status: Int): Result<*> {
        log.info("切换店铺营业状态：{}", if (status == 1) "营业中" else "打烊中")
        redisTemplate!!.opsForValue().set(KEY, status)
        return Result.success()
    }

    @get:GetMapping("/status")
    val status: Result<Int>
        get() {
            val status = redisTemplate!!.opsForValue()[KEY] as Int
            log.info("查询当前店铺营业状态")
            return Result.success(status)
        }

    companion object {
        const val KEY: String = "SHOP_STATUS"
    }
}
