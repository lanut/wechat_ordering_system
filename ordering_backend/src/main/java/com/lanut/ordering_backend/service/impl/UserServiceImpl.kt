package com.lanut.ordering_backend.service.impl

import com.lanut.ordering_backend.entity.User
import com.lanut.ordering_backend.mapper.UserMapper
import com.lanut.ordering_backend.service.IUserService
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
open class UserServiceImpl : ServiceImpl<UserMapper, User>(), IUserService
