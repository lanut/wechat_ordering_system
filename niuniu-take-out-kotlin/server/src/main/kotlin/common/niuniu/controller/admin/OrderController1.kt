package common.niuniu.controller.admin

import common.niuniu.dto.OrderCancelDTO
import common.niuniu.dto.OrderConfirmDTO
import common.niuniu.dto.OrderPageDTO
import common.niuniu.dto.OrderRejectionDTO
import common.niuniu.result.PageResult
import common.niuniu.result.Result
import common.niuniu.service.OrderService
import common.niuniu.utils.logInfo
import common.niuniu.vo.OrderStatisticsVO
import common.niuniu.vo.OrderVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController("adminOrderController")
@RequestMapping("/admin/order")
class OrderController {
    @Autowired
    private lateinit var orderService: OrderService

    /**
     * 条件分页查询订单信息
     * @param orderPageDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    fun conditionSearch(orderPageDTO: OrderPageDTO): Result<PageResult> {
        logInfo("条件分页查询订单信息：$orderPageDTO")
        val pageResult = orderService.conditionSearch(orderPageDTO)!!
        return Result.success(pageResult)
    }

    /**
     * 不同状态订单数量统计
     * @return
     */
    @GetMapping("/statistics")
    fun statistics(): Result<OrderStatisticsVO> {
        val orderStatisticsVO = orderService.statistics()!!
        return Result.success(orderStatisticsVO)
    }

    /**
     * 根据订单id查询订单信息
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    fun details(@PathVariable id: Int): Result<OrderVO> {
        logInfo("根据订单id查询订单详情")
        val orderVO = orderService.getById(id)!!
        return Result.success(orderVO)
    }

    /**
     * 接单
     * @param orderConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    fun confirm(@RequestBody orderConfirmDTO: OrderConfirmDTO?): Result<*> {
        logInfo("修改订单状态为接单：$orderConfirmDTO")
        orderService.confirm(orderConfirmDTO)
        return Result.success()
    }

    /**
     * 拒单
     * @param orderRejectionDTO
     * @return
     */
    @PutMapping("/reject")
    fun reject(@RequestBody orderRejectionDTO: OrderRejectionDTO?): Result<*> {
        logInfo("拒单，有原因：$orderRejectionDTO")
        orderService.reject(orderRejectionDTO)
        return Result.success()
    }

    /**
     * 取消订单
     * @param orderCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    fun cancel(@RequestBody orderCancelDTO: OrderCancelDTO?): Result<*> {
        logInfo("取消订单，有原因：$orderCancelDTO")
        orderService.cancel(orderCancelDTO)
        return Result.success()
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    fun delivery(@PathVariable id: Int?): Result<*> {
        logInfo("派送中：$id")
        orderService.delivery(id)
        return Result.success()
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    fun complete(@PathVariable id: Int?): Result<*> {
        logInfo("已完成：$id")
        orderService.complete(id)
        return Result.success()
    }
}
