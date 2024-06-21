package com.lanut.ordering_backend.entity.dto

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 *
 * </p>
 * @param userId 用户ID
 * @param openid 用户的openid
 * @param nickname 用户的昵称
 * @param avatarUrl 用户的头像
 * @param role 用户的角色: `admin`或`customer`
 * @param phoneNumber 用户的电话号码
 * @param registerDate 用户的注册日期
 * @param lastLogin 用户的最后登录日期
 *
 * @author lanut
 * @since 2024-06-04
 */
class User(
    @TableId(value = "user_id", type = IdType.AUTO)
    var userId: Int,
    var openid: String,
    var nickname: String,
    var avatarUrl: String,
    var role: String, // 用户角色: `admin` `customer`
    var phoneNumber: String?,
    var registerDate: LocalDateTime,
    var lastLogin: LocalDateTime,
) : Serializable  {
    override fun toString(): String {
        return "User{" +
        "userId=" + userId +
        ", openid=" + openid +
        ", nickname=" + nickname +
        ", avatar_url=" + avatarUrl +
        ", role=" + role +
        ", phoneNumber=" + phoneNumber +
        ", registerDate=" + registerDate +
        ", lastLogin=" + lastLogin +
        "}"
    }
}
