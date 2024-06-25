package com.lanut.ordering_backend.mapper

import com.lanut.ordering_backend.entity.dto.Dish
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import java.math.BigDecimal

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Mapper
interface DishMapper : BaseMapper<Dish> {
    @Insert("insert into dish (category_id, dish_name, price, description, image_url, status) values (#{categoryId}, #{dishName}, #{price}, #{description}, #{imageUrl}, #{status})")
    fun insert(
        categoryId: Int,
        dishName: String,
        price: BigDecimal,
        description: String?,
        imageUrl: String?,
        status: String
    ): Int

    @Select("select * from dish where dish_name = #{dishName}")
    fun selectByDishName(dishName: String): Dish?

    @Select("select dish_name from dish where dish_id = #{dishId}")
    fun getDishNameByDishId(dishId: Int): String
}
