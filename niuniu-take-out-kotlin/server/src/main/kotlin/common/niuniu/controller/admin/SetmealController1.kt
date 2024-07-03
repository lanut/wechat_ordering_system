package common.niuniu.controller.admin

import common.niuniu.annotation.Slf4j
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.dto.SetmealDTO
import common.niuniu.dto.SetmealPageDTO
import common.niuniu.result.PageResult
import common.niuniu.result.Result
import common.niuniu.service.SetmealService
import common.niuniu.vo.SetmealVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
class SetmealController{
    @Autowired
    private val setmealService: SetmealService? = null

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @CacheEvict(cacheNames = ["setmealCache"], key = "#setmealDTO.categoryId")
    fun addSetmeal(@RequestBody setmealDTO: SetmealDTO?): Result<*> {
        log.info("新增套餐的信息：{}", setmealDTO)
        setmealService!!.addSetmeal(setmealDTO)
        return Result.success()
    }

    /**
     * 套餐条件分页查询
     * @param setmealPageDTO
     * @return
     */
    @GetMapping("/page")
    fun getPageList(setmealPageDTO: SetmealPageDTO?): Result<PageResult> {
        log.info("条件分页查询：{}", setmealPageDTO)
        val pageResult = setmealService!!.getPageList(setmealPageDTO)!!
        return Result.success(pageResult)
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    fun getSetmealById(@PathVariable id: Int?): Result<SetmealVO> {
        log.info("要查询的套餐id：{}", id)
        val setmealVO = setmealService!!.getSetmealById(id)!!
        return Result.success(setmealVO)
    }

    /**
     * 根据id起售停售套餐
     * @param id
     * @return
     */
    @PutMapping("/status/{id}")
    @CacheEvict(cacheNames = ["setmealCache"], allEntries = true)
    fun onOff(@PathVariable id: Int?): Result<*> {
        log.info("套餐id:{}", id)
        setmealService!!.onOff(id)
        return Result.success()
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @CacheEvict(cacheNames = ["setmealCache"], allEntries = true)
    fun update(@RequestBody setmealDTO: SetmealDTO?): Result<*> {
        log.info("修改后的套餐信息：{}", setmealDTO)
        setmealService!!.update(setmealDTO)
        return Result.success()
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(cacheNames = ["setmealCache"], allEntries = true)
    fun deleteBatch(@RequestParam ids: List<Int?>?): Result<*> {
        log.info("批量删除套餐的套餐id集合：{}", ids)
        setmealService!!.deleteBatch(ids)
        return Result.success()
    }
}
