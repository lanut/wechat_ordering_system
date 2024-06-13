package com.lanut.ordering_backend.service

import com.lanut.ordering_backend.entity.dto.User
import com.lanut.ordering_backend.mapper.UserMapper
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userService: IUserService
) : UserDetailsService {

    override fun loadUserByUsername(openid: String): UserDetails {
        val user = userService.findByOpenid(openid)
            ?: throw UsernameNotFoundException("User with openid: $openid not found")

        return CustomUserDetails(user)
    }
}

data class CustomUserDetails(val user: User) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${user.role!!.toUpperCase()}"))
    }

    override fun getPassword(): String? {
        return null // OAuth2 不需要密码
    }

    override fun getUsername(): String {
        return user.openid!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
