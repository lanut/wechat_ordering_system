@file:Suppress("PropertyName")

package com.lanut.ordering_backend.entity.vo

import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderVO(
    val dishes: List<OrderDishVO>,
    val total_amount: BigDecimal
)

data class OrderDishVO(
    val dish_id: Int,
    val quantity: String
)

data class OrdersVO(
    val order_id: Int?,
    val order_date: LocalDateTime,
    val total_amount: BigDecimal,
    val order_status: String,
) {
    val dishes: MutableList<OrderedDishVO> = mutableListOf()
}

data class OrderedDishVO(
    val dish_id: Int?,
    val dish_name: String,
    val quantity: Int,
    val subtotal: BigDecimal,
)

