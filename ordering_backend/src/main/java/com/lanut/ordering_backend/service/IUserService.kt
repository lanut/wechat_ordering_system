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
    fun findByOpenid(openId: String): User?

    fun save(openid: String, nickname: String, role: String): Boolean
}
