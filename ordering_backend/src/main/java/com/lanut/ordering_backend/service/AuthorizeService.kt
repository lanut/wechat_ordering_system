package com.lanut.ordering_backend.service

import com.lanut.ordering_backend.entity.vo.VerifiedUser
import jakarta.annotation.Resource
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthorizeService : UserDetailsService {

    @Resource
    lateinit var userService: IUserService

    // 此处的username实际上为微信的openid
    override fun loadUserByUsername(username: String): UserDetails {
        // 添加jwt信息
        val user = userService.getUserByOpenid(username)
        if (user != null) {
            return VerifiedUser(user.nickname, user.openid, user.role)
        } else {
            throw IllegalArgumentException("用户不存在")
        }
    }

}