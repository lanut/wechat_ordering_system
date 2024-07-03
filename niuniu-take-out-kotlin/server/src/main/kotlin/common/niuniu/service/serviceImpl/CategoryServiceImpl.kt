package common.niuniu.service.serviceImpl

import com.github.pagehelper.PageHelper
import common.niuniu.dto.CategoryDTO
import common.niuniu.dto.CategoryTypePageDTO
import common.niuniu.mapper.CategoryMapper
import common.niuniu.po.Category
import common.niuniu.result.PageResult
import common.niuniu.service.CategoryService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl : CategoryService {
    @Autowired
    private lateinit var categoryMapper: CategoryMapper

    /**
     * 新增分类
     * @param categoryDTO
     */
    override fun addCategory(categoryDTO: CategoryDTO?) {
        val category = Category()
        BeanUtils.copyProperties(categoryDTO!!, category)
        category.status = 1 // 默认启用
        categoryMapper.add(category)
    }

    /**
     * 根据type条件分页查询
     * @param categoryTypePageDTO
     * @return
     */
    override fun getPageList(categoryTypePageDTO: CategoryTypePageDTO?): PageResult? {
        PageHelper.startPage<Any>(categoryTypePageDTO!!.page, categoryTypePageDTO.pageSize)
        val pagelist = categoryMapper.getPageList(categoryTypePageDTO)
        return PageResult(pagelist.total, pagelist.result)
    }

    /**
     * 获取所有分类列表
     * @return
     */
    override fun getList(type: Int): List<Category> {
        val categoryList = categoryMapper.getList(type)
        return categoryList
    }

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    override fun getById(id: Int?): Category? {
        return categoryMapper.getById(id)
    }

    /**
     * 更新分类信息
     * @param categoryDTO
     */
    override fun udpate(categoryDTO: CategoryDTO?) {
        val category = Category()
        BeanUtils.copyProperties(categoryDTO!!, category)
        categoryMapper.update(category)
    }

    /**
     * 根据id删除分类
     * @param id
     */
    override fun delete(id: Int?) {
        categoryMapper.delete(id)
    }

    /**
     * 分类起售/停售
     */
    override fun onOff(id: Int?) {
        categoryMapper.onOff(id)
    }
}
