package common.niuniu.controller.admin

import common.niuniu.dto.DishDTO
import common.niuniu.dto.DishPageDTO
import common.niuniu.result.PageResult
import common.niuniu.result.Result
import common.niuniu.service.DishService
import common.niuniu.utils.logInfo
import common.niuniu.vo.DishVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/dish")
class DishController {
    @Autowired
    private lateinit var dishService: DishService

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    /**
     * 添加菜品
     * @return
     */
    @PostMapping
    fun addDishWithFlavor(@RequestBody dishDTO: DishDTO): Result<*> {
        logInfo("要添加的菜品信息：$dishDTO")
        dishService.addDishWithFlavor(dishDTO)
        // 清理缓存数据
        val key = "dish_" + dishDTO.categoryId
        cleanCache(key)
        return Result.success()
    }

    /**
     * 菜品条件分页查询
     * @param dishPageDTO
     * @return
     */
    @GetMapping("/page")
    fun getPageList(dishPageDTO: DishPageDTO?): Result<PageResult> {
        logInfo("菜品dish分页条件page：$dishPageDTO")
        val pageResult = dishService.getPageList(dishPageDTO)!!
        return Result.success(pageResult)
    }

    /**
     * 根据id查询菜品和对应口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    fun getDishWithFlavorById(@PathVariable id: Int): Result<DishVO> {
        logInfo("根据id查询菜品：$id")
        val dishVO = dishService.getDishWithFlavorById(id)!!
        return Result.success(dishVO)
    }

    /**
     * 根据id修改起售停售状态
     * @param id
     * @return
     */
    @PutMapping("/status/{id}")
    fun onOff(@PathVariable id: Int): Result<*> {
        logInfo("根据id修改状态，$id")
        dishService.onOff(id)
        // 将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*")
        return Result.success()
    }

    /**
     * 更新菜品以及对应口味
     * @param dishDTO
     * @return
     */
    @PutMapping
    fun updateDishWithFlavor(@RequestBody dishDTO: DishDTO?): Result<*> {
        logInfo("用户传过来的新菜品数据，$dishDTO")
        dishService.updateDishWithFlavor(dishDTO)
        // 将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*")
        return Result.success()
    }

    /**
     * 根据ids批量删除菜品数据
     * @RequestParam 表示必须要ids参数，否则会报错
     * @param ids
     * @return
     */
    @DeleteMapping
    fun deleteBatch(@RequestParam ids: List<Int>): Result<*> {
        logInfo("要删除的菜品id列表，$ids")
        dishService.deleteBatch(ids)
        // 将所有的菜品缓存数据清理掉，所有以dish_开头的key
        cleanCache("dish_*")
        return Result.success()
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    fun cleanCache(pattern: String) {
        val keys = redisTemplate.keys(pattern)
        redisTemplate.delete(keys)
    }
}
