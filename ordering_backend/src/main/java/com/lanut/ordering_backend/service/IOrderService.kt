package com.lanut.ordering_backend.service

import com.lanut.ordering_backend.entity.dto.Order
import com.baomidou.mybatisplus.extension.service.IService
import com.lanut.ordering_backend.entity.vo.OrdersVO

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
interface IOrderService : IService<Order> {
    fun getUserOrders(openid: String): List<OrdersVO>
}
