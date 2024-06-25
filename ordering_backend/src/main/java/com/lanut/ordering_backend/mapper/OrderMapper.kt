package com.lanut.ordering_backend.mapper

import com.lanut.ordering_backend.entity.dto.Order
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
interface OrderMapper : BaseMapper<Order> {
    @Select("select * from `order` where user_id = (select user_id from user where openid = #{openid})")
    fun getUserOrders(openid: String): List<Order>

}
