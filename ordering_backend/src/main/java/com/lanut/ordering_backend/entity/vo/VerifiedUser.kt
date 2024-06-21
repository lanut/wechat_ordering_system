package com.lanut.ordering_backend.entity.vo

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

// TODO: 添加jwt信息
data class VerifiedUser(
    val nickname: String,
    val openid: String,
    val role: Role,
) : UserDetails {
    constructor(nickname: String, openid: String, role: String) : this(
        nickname,
        openid,
        when (role) {
            "admin" -> Role.ADMIN
            "customer" -> Role.CUSTOMER
            else -> throw IllegalArgumentException("Role 不合法")
        }
    )


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(role)
    }

    override fun getPassword(): String {
        return "{noop}"
    }

    // 实际上为获取 `nickname` 字段
    override fun getUsername(): String {
        return nickname
    }
}


enum class Role(private val role: String) : GrantedAuthority {
    ADMIN("admin"), CUSTOMER("customer"), ;

    override fun getAuthority(): String {
        return role
    }
}


