package com.lanut.ordering_backend.mapper

import com.lanut.ordering_backend.entity.dto.OrderDetail
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
interface OrderDetailMapper : BaseMapper<OrderDetail> {
    @Select("select * from order_detail where order_id = #{orderId}")
    fun getOrderDetailsByOrderId(orderId: Int): List<OrderDetail>

}
