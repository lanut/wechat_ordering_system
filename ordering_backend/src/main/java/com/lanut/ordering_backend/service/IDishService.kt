package com.lanut.ordering_backend.service

import com.lanut.ordering_backend.entity.dto.Dish
import com.baomidou.mybatisplus.extension.service.IService

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
interface IDishService : IService<Dish> {
    fun getAllDishes(): List<Dish>
    fun addDish(dishMap: Map<String, String>): Dish?
}
