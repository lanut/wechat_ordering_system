package common.niuniu.controller.user

import common.niuniu.annotation.Slf4j
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.po.Setmeal
import common.niuniu.result.Result
import common.niuniu.service.SetmealService
import common.niuniu.vo.DishItemVO
import common.niuniu.vo.SetmealWithPicVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
class SetmealController {
    @Autowired
    private val setmealService: SetmealService? = null

    /**
     * 根据分类id查询所有套餐
     * @param id
     * @return
     */
    @GetMapping("/list/{id}")
    @Cacheable(cacheNames = ["setmealCache"], key = "#id")
    fun getSetmealList(@PathVariable id: Int?): Result<List<Setmeal>> {
        log.info("要查询的套餐分类id:{}", id)
        val setmealList = setmealService!!.getList(id)!!
        return Result.success(setmealList)
    }

    /**
     * 根据套餐id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    fun getSetmeal(@PathVariable id: Int?): Result<SetmealWithPicVO> {
        log.info("根据套餐id查询该套餐：{}", id)
        val setmealWithPicVO = setmealService!!.getSetmealWithPic(id)!!
        return Result.success(setmealWithPicVO)
    }

    /**
     * 根据套餐查询包含的菜品
     * @return
     */
    @GetMapping("/dish/{id}")
    fun getSetmealDishes(@PathVariable id: Int?): Result<List<DishItemVO>> {
        log.info("套餐id:{}", id)
        val dishes = setmealService!!.getSetmealDishesById(id)!!
        return Result.success(dishes)
    }
}
