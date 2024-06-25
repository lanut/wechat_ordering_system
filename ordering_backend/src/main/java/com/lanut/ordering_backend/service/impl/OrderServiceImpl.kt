package com.lanut.ordering_backend.service.impl

import com.lanut.ordering_backend.entity.dto.Order
import com.lanut.ordering_backend.mapper.OrderMapper
import com.lanut.ordering_backend.service.IOrderService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lanut.ordering_backend.entity.vo.OrderedDishVO
import com.lanut.ordering_backend.entity.vo.OrdersVO
import com.lanut.ordering_backend.mapper.DishMapper
import com.lanut.ordering_backend.mapper.OrderDetailMapper
import jakarta.annotation.Resource
import org.springframework.stereotype.Service

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Service
open class OrderServiceImpl : ServiceImpl<OrderMapper, Order>(), IOrderService {

    @Resource
    lateinit var orderMapper: OrderMapper

    @Resource
    lateinit var orderDetailMapper: OrderDetailMapper

    @Resource
    lateinit var dishMapper: DishMapper

    override fun getUserOrders(openid: String): List<OrdersVO> {
        // 通过openid获取用户的所有订单
        val orders = orderMapper.getUserOrders(openid)
        // 转换为OrdersVO
        val ordersVO = mutableListOf<OrdersVO>()
        orders.forEach { order: Order ->
            // 通过订单id获取订单详情
            val orderVO = OrdersVO(order.orderId, order.orderDate, order.totalAmount, order.orderStatus)
            ordersVO.add(orderVO)
            // 再添加订单详情
            val orderDetails = orderDetailMapper.getOrderDetailsByOrderId(order.orderId)
            orderDetails.forEach { orderDetail ->
                // 通过dishId获取dishName
                val dishName = dishMapper.getDishNameByDishId(orderDetail.dishId)
                // 添加到OrderDishVO
                val orderDishVO = OrderedDishVO(orderDetail.dishId, dishName, orderDetail.quantity, orderDetail.subtotal)
                // 添加到OrderVO
                orderVO.dishes.add(orderDishVO)
            }
        }
        return ordersVO
    }

}
