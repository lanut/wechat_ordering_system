package com.lanut.ordering_backend.service.impl

import com.lanut.ordering_backend.entity.Category
import com.lanut.ordering_backend.mapper.CategoryMapper
import com.lanut.ordering_backend.service.ICategoryService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
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
open class CategoryServiceImpl : ServiceImpl<CategoryMapper, Category>(), ICategoryService
