package com.lanut.ordering_backend.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
class User : Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    var userId: Int? = null

    var username: String? = null

    var password: String? = null

    var role: String? = null

    var email: String? = null

    var phoneNumber: String? = null

    var registerDate: LocalDateTime? = null

    var lastLogin: LocalDateTime? = null

    override fun toString(): String {
        return "User{" +
        "userId=" + userId +
        ", username=" + username +
        ", password=" + password +
        ", role=" + role +
        ", email=" + email +
        ", phoneNumber=" + phoneNumber +
        ", registerDate=" + registerDate +
        ", lastLogin=" + lastLogin +
        "}"
    }
}
