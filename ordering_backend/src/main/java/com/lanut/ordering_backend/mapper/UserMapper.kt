package com.lanut.ordering_backend.mapper

import com.lanut.ordering_backend.entity.dto.User
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Mapper
interface UserMapper : BaseMapper<User> {
    @Select("select * from user where openid = #{openid}")
    fun getUserByOpenId(openid: String): User?

    @Select("select * from user where nickname = #{username}")
    fun getUserByUsername(username: String): User?
}
