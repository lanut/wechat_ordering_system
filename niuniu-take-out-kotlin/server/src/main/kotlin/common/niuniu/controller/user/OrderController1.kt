package common.niuniu.controller.user

import common.niuniu.annotation.Slf4j
import common.niuniu.annotation.Slf4j.Companion.log
import common.niuniu.dto.OrderPaymentDTO
import common.niuniu.dto.OrderSubmitDTO
import common.niuniu.result.PageResult
import common.niuniu.result.Result
import common.niuniu.service.OrderService
import common.niuniu.vo.OrderPaymentVO
import common.niuniu.vo.OrderSubmitVO
import common.niuniu.vo.OrderVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
class OrderController {
    @Autowired
    private val orderService: OrderService? = null

    /**
     * 用户下单
     *
     * @param orderSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    fun submit(@RequestBody orderSubmitDTO: OrderSubmitDTO?): Result<OrderSubmitVO> {
        log.info("用户传过来的订单信息：{}", orderSubmitDTO)
        val orderSubmitVO = orderService!!.submit(orderSubmitDTO)!!
        return Result.success(orderSubmitVO)
    }

    /**
     * 当前用户未支付订单数量
     *
     * @return
     */
    @GetMapping("/unPayOrderCount")
    fun unPayOrderCount(): Result<Int> {
        log.info("查询当前用户未支付订单数量")
        return Result.success(orderService!!.unPayOrderCount()!!)
    }

    /**
     * 订单支付
     *
     * @param orderPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @Throws(Exception::class)
    fun payment(@RequestBody orderPaymentDTO: OrderPaymentDTO?): Result<OrderPaymentVO> {
        log.info("订单支付：{}", orderPaymentDTO)
        val orderPaymentVO = orderService!!.payment(orderPaymentDTO)
        log.info("生成预支付交易单：{}", orderPaymentVO)
        return Result.success(orderPaymentVO!!)
    }

    /**
     * 根据id查询订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    fun getById(@PathVariable id: Int?): Result<OrderVO> {
        log.info("订单id:{}", id)
        val orderVO = orderService!!.getById(id)
        return Result.success(orderVO!!)
    }

    /**
     * 条件分页查询历史订单
     *
     * @param page
     * @param pageSize
     * @param status   订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * @return
     */
    @GetMapping("/historyOrders")
    fun page(page: Int, pageSize: Int, status: Int?): Result<PageResult> {
        val pageResult = orderService!!.userPage(page, pageSize, status)
        return Result.success(pageResult!!)
    }

    /**
     * 用户取消订单
     *
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @Throws(Exception::class)
    fun cancel(@PathVariable id: Int?): Result<*> {
        log.info("用户取消订单，手动取消或者超时，id为：{}", id)
        orderService!!.userCancelById(id)
        return Result.success()
    }

    /**
     * 再来一单
     *
     * @param id
     * @return
     */
    @PostMapping("/reOrder/{id}")
    fun reOrder(@PathVariable id: Int?): Result<*> {
        log.info("根据订单id再来一单：{}", id)
        orderService!!.reOrder(id)
        return Result.success()
    }


    /**
     * 用户催单
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    fun reminder(@PathVariable id: Int): Result<*> {
        log.info("用户催单，订单id:{}", id)
        orderService!!.reminder(id)
        return Result.success()
    }
}
