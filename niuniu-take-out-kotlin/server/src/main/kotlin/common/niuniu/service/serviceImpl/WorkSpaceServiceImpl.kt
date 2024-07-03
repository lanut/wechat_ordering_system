package common.niuniu.service.serviceImpl

import common.niuniu.mapper.DishMapper
import common.niuniu.mapper.OrderMapper
import common.niuniu.mapper.SetmealMapper
import common.niuniu.mapper.UserMapper
import common.niuniu.po.Order
import common.niuniu.service.WorkSpaceService
import common.niuniu.vo.BusinessDataVO
import common.niuniu.vo.DishOverViewVO
import common.niuniu.vo.OrderOverViewVO
import common.niuniu.vo.SetmealOverViewVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class WorkSpaceServiceImpl : WorkSpaceService {
    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var orderMapper: OrderMapper

    @Autowired
    private lateinit var dishMapper: DishMapper

    @Autowired
    private lateinit var setmealMapper: SetmealMapper

    /**
     * 工作台今日数据总览
     * @param begin
     * @param end
     * @return
     */
    override fun getBusinessData(begin: LocalDateTime, end: LocalDateTime): BusinessDataVO? {
        /**
         * 营业额：当日已完成订单的总金额
         * 有效订单：当日已完成订单的数量
         * 订单完成率：有效订单数 / 总订单数
         * 平均客单价：营业额 / 有效订单数
         * 新增用户：当日新增用户的数量
         */

        val map: MutableMap<String, Any> = mutableMapOf()
        map["begin"] = begin
        map["end"] = end
        // 查询总订单数
        val totalOrderCount = orderMapper.countByMap(map)
        map["status"] = Order.COMPLETED
        // 1、营业额
        var turnover = orderMapper.sumByMap(map)
        turnover = turnover ?: 0.0
        // 2、有效订单数
        val validOrderCount = orderMapper.countByMap(map)
        var orderCompletionRate = 0.0
        var unitPrice = 0.0
        if (totalOrderCount != 0 && validOrderCount != 0) {
            // 3、订单完成率
            orderCompletionRate = validOrderCount!!.toDouble() / totalOrderCount!!
            // 4、平均客单价
            unitPrice = turnover / validOrderCount
        }
        // 5、新增用户数
        val newUsers = userMapper.countByMap(map)
        return BusinessDataVO.builder()
            .turnover(turnover)
            .validOrderCount(validOrderCount)
            .orderCompletionRate(orderCompletionRate)
            .unitPrice(unitPrice)
            .newUsers(newUsers)
            .build()
    }

    override val orderOverView: OrderOverViewVO
        /**
         * 订单数据情况
         * @return
         */
        get() {
            /**
             * 全部订单 待接单 待派送 已完成 已取消
             */
            val map: MutableMap<String, Any> = mutableMapOf()
            map["begin"] = LocalDateTime.now().with(LocalTime.MIN)
            val allOrders = orderMapper.countByMap(map)

            map["status"] = Order.TO_BE_CONFIRMED
            val toConfirmed = orderMapper.countByMap(map)
            map["status"] = Order.CONFIRMED
            val toDelivery = orderMapper.countByMap(map)
            map["status"] = Order.COMPLETED
            val completed = orderMapper.countByMap(map)
            map["status"] = Order.CANCELLED
            val canceled = orderMapper.countByMap(map)

            return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .waitingOrders(toConfirmed)
                .deliveredOrders(toDelivery)
                .completedOrders(completed)
                .cancelledOrders(canceled)
                .build()
        }

    override val dishOverView: DishOverViewVO
        /**
         * 菜品总览
         * @return
         */
        get() {
            /**
             * 已起售 已停售
             */
            val on = dishMapper.getByStatus(1)
            val off = dishMapper.getByStatus(0)
            return DishOverViewVO.builder()
                .sold(on)
                .discontinued(off)
                .build()
        }

    override val setmealOverView: SetmealOverViewVO
        /**
         * 套餐总览
         * @return
         */
        get() {
            /**
             * 已起售 已停售
             */
            val on = setmealMapper.getByStatus(1)
            val off = setmealMapper.getByStatus(0)
            return SetmealOverViewVO.builder()
                .sold(on)
                .discontinued(off)
                .build()
        }
}
