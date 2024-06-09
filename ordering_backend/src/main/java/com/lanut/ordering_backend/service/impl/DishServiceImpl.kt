package com.lanut.ordering_backend.service.impl

import com.lanut.ordering_backend.entity.Dish
import com.lanut.ordering_backend.mapper.DishMapper
import com.lanut.ordering_backend.service.IDishService
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
open class DishServiceImpl : ServiceImpl<DishMapper, Dish>(), IDishService
