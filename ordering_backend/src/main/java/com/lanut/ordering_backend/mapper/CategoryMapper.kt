package com.lanut.ordering_backend.mapper

import com.lanut.ordering_backend.entity.dto.Category
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Mapper
interface CategoryMapper : BaseMapper<Category>
