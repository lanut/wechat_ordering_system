package common.niuniu.controller.user

import common.niuniu.annotation.Slf4j
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.dto.CartDTO
import common.niuniu.po.Cart
import common.niuniu.result.Result
import common.niuniu.service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/cart")
@Slf4j
class CartController {
    @Autowired
    private val cartService: CartService? = null

    @PostMapping("/add")
    fun add(@RequestBody cartDTO: CartDTO?): Result<*> {
        log.info("将如下菜品/套餐添加进购物车：{}", cartDTO)
        cartService!!.add(cartDTO)
        return Result.success()
    }

    @PutMapping("/sub")
    fun sub(@RequestBody cartDTO: CartDTO?): Result<*> {
        log.info("将如下菜品/套餐数量减一：{}", cartDTO)
        cartService!!.sub(cartDTO)
        return Result.success()
    }

    @get:GetMapping("/list")
    val list: Result<List<Cart>>
        get() {
            log.info("拿到当前用户的购物车列表")
            val cartList = cartService!!.list?: emptyList()
            return Result.success(cartList)
        }

    @DeleteMapping("/clean")
    fun cleanCart(): Result<*> {
        log.info("清空购物车")
        cartService!!.clean()
        return Result.success()
    }
}
