package common.niuniu.controller.admin

import common.niuniu.dto.CategoryDTO
import common.niuniu.dto.CategoryTypePageDTO
import common.niuniu.po.*
import common.niuniu.result.PageResult
import common.niuniu.result.Result
import common.niuniu.service.CategoryService
import common.niuniu.utils.logInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/category")
class CategoryController {
    @Autowired
    private val categoryService: CategoryService? = null

    /**
     * 新增分类
     * @return
     */
    @PostMapping
    fun addCategory(@RequestBody categoryDTO: CategoryDTO?): Result<*> {
        categoryService!!.addCategory(categoryDTO)
        return Result.success()
    }

    /**
     * 分类条件分页查询
     * @param categoryTypePageDTO
     * @return
     */
    @GetMapping("/page")
    fun getPageList(categoryTypePageDTO: CategoryTypePageDTO?): Result<PageResult> {
        logInfo("用户传过来的带条件的page分页参数：${categoryTypePageDTO}")
        val pageResult = categoryService!!.getPageList(categoryTypePageDTO)!!
        return Result.success(pageResult)
    }

    /**
     * 根据id查询指定分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int?): Result<Category> {
        logInfo("根据id查询分类：$id")
        val category = categoryService!!.getById(id)!!
        return Result.success(category)
    }

    /**
     * 起售/停售
     * @return
     */
    @PutMapping("/status/{id}")
    fun onOff(@PathVariable id: Int?): Result<*> {
        categoryService!!.onOff(id)
        return Result.success()
    }

    /**
     * 更新分类信息
     * @param categoryDTO
     * @return
     */
    @PutMapping
    fun udpate(@RequestBody categoryDTO: CategoryDTO?): Result<*> {
        logInfo("拿到更新后的信息，$categoryDTO")
        categoryService!!.udpate(categoryDTO)
        return Result.success()
    }

    /**
     * 根据id修改分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int?): Result<*> {
        logInfo("根据id删除分类：$id")
        categoryService!!.delete(id)
        return Result.success()
    }
}
