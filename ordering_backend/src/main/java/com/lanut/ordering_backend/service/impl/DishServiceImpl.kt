package com.lanut.ordering_backend.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lanut.ordering_backend.entity.dto.Dish
import com.lanut.ordering_backend.mapper.DishMapper
import com.lanut.ordering_backend.service.IDishService
import jakarta.annotation.Resource
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
open class DishServiceImpl : ServiceImpl<DishMapper, Dish>(), IDishService {

    @Resource
    lateinit var mapper: DishMapper

    override fun getAllDishes(): List<Dish> {
        return mapper.selectList(null).toList()
    }

    override fun addDish(dishMap: Map<String, String>): Dish? {
        val categoryId = dishMap["category_id"]?.toInt() ?: return null
        val dishName = dishMap["dish_name"] ?: return null
        val price = dishMap["price"]?.toBigDecimal() ?: return null
        val description = dishMap["description"]
        val imageUrl = dishMap["image_url"]
        val status = dishMap["status"] ?: "available"
        val insert = mapper.insert(categoryId, dishName, price, description, imageUrl, status)
        if (insert <= 0) return null
        return mapper.selectByDishName(dishName)
    }
}
