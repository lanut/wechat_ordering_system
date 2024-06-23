package com.lanut.ordering_backend.mapper

import com.lanut.ordering_backend.entity.dto.Category
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.lanut.ordering_backend.entity.dto.Dish
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Mapper
interface CategoryMapper : BaseMapper<Category> {
    @Select("select * from dish where category_id = #{categoryId}")
    fun getDishesByCategory(categoryId: Int): List<Dish>
}
