package com.lanut.ordering_backend.entity.dto

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
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
@TableName("`order`")
class Order(
    @TableId(value = "order_id", type = IdType.AUTO)
    var orderId: Int,
    var userId: Int,
    var orderDate: LocalDateTime,
    var totalAmount: BigDecimal,
    var orderStatus: String,
    var tableId: Int,
) : Serializable {
    override fun toString(): String {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", tableId=" + tableId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", orderStatus=" + orderStatus +
                "}"
    }
}
