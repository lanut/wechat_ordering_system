package com.lanut.ordering_backend.entity.dto

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable
import java.math.BigDecimal

/**
 * <p>
 *
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@TableName("order_detail")
class OrderDetail(
    @TableId(value = "order_detail_id", type = IdType.AUTO) var orderDetailId: Int,
    var orderId: Int,
    var dishId: Int,
    var quantity: Int,
    var subtotal: BigDecimal,
) : Serializable {
    override fun toString(): String {
        return "OrderDetail{orderDetailId=$orderDetailId, orderId=$orderId, dishId=$dishId, quantity=$quantity, subtotal=$subtotal}"
    }
}
