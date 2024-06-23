package com.lanut.ordering_backend.service

import com.lanut.ordering_backend.entity.dto.Category
import com.baomidou.mybatisplus.extension.service.IService
import com.lanut.ordering_backend.entity.dto.Dish

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
interface ICategoryService : IService<Category> {
    fun getAllCategories(): List<Category>
    fun getDishesByCategory(categoryId: Int): List<Dish>
}
