package common.niuniu.task

import common.niuniu.mapper.OrderMapper
import common.niuniu.po.Order
import common.niuniu.utils.logInfo
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer

/**
 * 自定义定时任务，实现订单状态定时处理
 */
@Component
@Slf4j
class OrderTask {
    @Autowired
    private lateinit var orderMapper: OrderMapper

    /**
     * 处理支付超时订单
     */
    @Scheduled(cron = "0 * * * * ?") // 表示每分钟第0秒触发
    fun processTimeoutOrder() {
        logInfo("处理支付超时订单：${Date()}")
        val time = LocalDateTime.now().plusMinutes(-15)
        // 每分钟，查询待支付并且超过15分钟的所有订单
        // select * from orders where status = 1 and order_time < 当前时间-15分钟
        val ordersList = orderMapper.getByStatusAndOrderTimeLT(Order.PENDING_PAYMENT, time)
        // 超时的订单要改为取消，并设置取消原因和取消时间
        if (!ordersList.isNullOrEmpty()) {
            ordersList.forEach(Consumer { order: Order? ->
                order!!.status = Order.CANCELLED
                order.cancelReason = "支付超时，自动取消"
                order.cancelTime = LocalDateTime.now()
                orderMapper.update(order)
            })
        }
    }

    /**
     * 处理“派送中”状态的订单
     */
    @Scheduled(cron = "0 0 1 * * ?") // 表示每次1:00:00触发
    fun processDeliveryOrder() {
        logInfo("处理派送中订单：${Date()}")
        // 每日凌晨1点，查询正在派送中并且下单时间超过1小时的所有订单
        // select * from orders where status = 4 and order_time < 当前时间-1小时
        val time = LocalDateTime.now().plusMinutes(-60)
        val ordersList = orderMapper.getByStatusAndOrderTimeLT(Order.DELIVERY_IN_PROGRESS, time)
        // 将其状态都改为已完成
        if (!ordersList.isNullOrEmpty()) {
            ordersList.forEach(Consumer { order: Order? ->
                order!!.status = Order.COMPLETED
                orderMapper.update(order)
            })
        }
    }
}