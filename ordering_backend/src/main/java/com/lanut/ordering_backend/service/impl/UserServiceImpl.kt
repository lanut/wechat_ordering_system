package com.lanut.ordering_backend.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lanut.ordering_backend.entity.dto.User
import com.lanut.ordering_backend.mapper.UserMapper
import com.lanut.ordering_backend.service.IUserService
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
open class UserServiceImpl : ServiceImpl<UserMapper, User>(), IUserService {
    @Resource
    lateinit var userMapper: UserMapper

    override fun findByOpenid(openid: String): User? {
        return baseMapper.getUserByOpenId(openid)
    }

    override fun save(openid: String, nickname: String, role: String): Boolean {
        return userMapper.saveNewUser(openid, nickname, role)
    }
}
