package common.niuniu.controller.user

import common.niuniu.annotation.Slf4j
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.constant.StatusConstant
import common.niuniu.po.Dish
import common.niuniu.result.Result
import common.niuniu.service.DishService
import common.niuniu.vo.DishVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
class DishController {
    @Autowired
    private val dishService: DishService? = null

    @Autowired
    private val redisTemplate: RedisTemplate<String, Any>? = null

    /**
     * 根据分类id查询该分类下的所有菜品
     *
     * @return
     */
    @GetMapping("/list/{id}")
    fun getDishList(@PathVariable id: Int): Result<List<DishVO>> {
        log.info("要查询当前的分类categoryId下的所有商品：{}", id)
        // 构造redis中的key，规则：dish_分类id
        val key = "dish_$id"
        // 查询redis中是否存在菜品数据
        var dishes = redisTemplate!!.opsForValue()[key] as List<DishVO>?
        if (!dishes.isNullOrEmpty()) {
            //如果存在，直接返回，无须查询数据库
            return Result.success(dishes)
        }
        // 用户端除了分类条件限制，且只能展示起售中的商品，因此还有status限制
        val dish = Dish()
        dish.categoryId = id
        dish.status = StatusConstant.ENABLE
        // 如果不存在，查询数据库，将查询到的数据放入redis中
        dishes = dishService!!.getDishesWithFlavorById(dish)
        redisTemplate.opsForValue().set(key, dishes)
        return Result.success(dishes)
    }

    /**
     * 根据菜品id查询菜品详情和对应口味
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    fun getDish(@PathVariable id: Int?): Result<DishVO> {
        log.info("用户根据菜品id查询菜品详情和对应口味：{}", id)
        val dishVO = dishService!!.getDishWithFlavorById(id)!!
        return Result.success(dishVO)
    }
}
