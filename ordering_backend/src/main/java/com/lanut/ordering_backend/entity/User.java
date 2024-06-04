package com.lanut.ordering_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String username;

    private String password;

    private String role;

    private String email;

    private String phoneNumber;

    private LocalDateTime registerDate;

    private LocalDateTime lastLogin;

    @Override
    public String toString() {
        return "User{" +
        "userId = " + userId +
        ", username = " + username +
        ", password = " + password +
        ", role = " + role +
        ", email = " + email +
        ", phoneNumber = " + phoneNumber +
        ", registerDate = " + registerDate +
        ", lastLogin = " + lastLogin +
        "}";
    }
}
