package com.lanut.ordering_backend.service


import jakarta.annotation.Resource
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.Collections

class CustomOAuth2UserService : DefaultOAuth2UserService() {

    @Resource
    lateinit var userService: IUserService

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest) // 调用父类的方法
        val attributes = oAuth2User.attributes // 获取用户信息
        val openid = attributes["openid"] as String // 微信的openid
        val nickname = attributes["nickname"] as String // 微信昵称

        // 在这里处理用户信息，如自动注册用户
        val user = userService.findByOpenid(openid)
        if (user == null) {
            userService.save(openid = openid, nickname = nickname, role = "customer")
        }

        // 返回用户信息
        return DefaultOAuth2User(
            Collections.singleton(SimpleGrantedAuthority("ROLE_USER")),
            attributes, "openid"
        )
    }
}
