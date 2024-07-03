package common.niuniu.controller.user

import common.niuniu.annotation.Slf4j
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.po.*
import common.niuniu.result.Result
import common.niuniu.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
class CategoryController {
    @Autowired
    private val categoryService: CategoryService? = null

    /**
     * 获取所有分类数据
     * @return
     */
    @GetMapping("/list")
    fun list(type: Int = 1): Result<List<Category>> {
        log.info("用户想要查询的分类（套餐/菜品分类？）：{}", type)
        val categoryList = categoryService!!.getList(type)
        return Result.success(categoryList)
    }
}
