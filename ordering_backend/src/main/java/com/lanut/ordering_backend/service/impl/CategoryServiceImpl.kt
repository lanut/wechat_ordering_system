package com.lanut.ordering_backend.service.impl

import com.lanut.ordering_backend.entity.dto.Category
import com.lanut.ordering_backend.mapper.CategoryMapper
import com.lanut.ordering_backend.service.ICategoryService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lanut.ordering_backend.entity.dto.Dish
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Service
open class CategoryServiceImpl : ServiceImpl<CategoryMapper, Category>(), ICategoryService {

    @Autowired
    lateinit var categoryMapper: CategoryMapper

    override fun getAllCategories(): List<Category> {
        return categoryMapper.selectList(null)
    }

    override fun getDishesByCategory(categoryId: Int): List<Dish> {
        return categoryMapper.getDishesByCategory(categoryId)
    }
}
