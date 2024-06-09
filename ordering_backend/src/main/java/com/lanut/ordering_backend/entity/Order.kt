package com.lanut.ordering_backend.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * <p>
 * 
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
class Order : Serializable {

    @TableId(value = "order_id", type = IdType.AUTO)
    var orderId: Int? = null

    var userId: Int? = null

    var orderDate: LocalDateTime? = null

    var totalAmount: BigDecimal? = null

    var orderStatus: String? = null

    override fun toString(): String {
        return "Order{" +
        "orderId=" + orderId +
        ", userId=" + userId +
        ", orderDate=" + orderDate +
        ", totalAmount=" + totalAmount +
        ", orderStatus=" + orderStatus +
        "}"
    }
}
