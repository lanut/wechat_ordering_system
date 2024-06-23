package com.lanut.ordering_backend.service

import com.lanut.ordering_backend.entity.dto.User
import com.baomidou.mybatisplus.extension.service.IService

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
interface IUserService : IService<User> {
    fun getUserByOpenid(openid: String): User?

    fun tokenToUser(token: String): User
}
