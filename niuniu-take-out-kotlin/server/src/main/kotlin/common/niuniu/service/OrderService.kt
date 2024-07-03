package common.niuniu.service

import common.niuniu.dto.*
import common.niuniu.result.PageResult
import common.niuniu.vo.OrderPaymentVO
import common.niuniu.vo.OrderStatisticsVO
import common.niuniu.vo.OrderSubmitVO
import common.niuniu.vo.OrderVO

interface OrderService {
    fun submit(orderSubmitDTO: OrderSubmitDTO?): OrderSubmitVO?

    fun unPayOrderCount(): Int?

    fun getById(id: Int?): OrderVO?

    fun userPage(page: Int, pageSize: Int, status: Int?): PageResult?

    @Throws(Exception::class)
    fun userCancelById(id: Int?)

    fun reOrder(id: Int?)

    fun payment(orderPaymentDTO: OrderPaymentDTO?): OrderPaymentVO?

    fun conditionSearch(orderPageDTO: OrderPageDTO?): PageResult?

    fun statistics(): OrderStatisticsVO?

    fun confirm(orderConfirmDTO: OrderConfirmDTO?)

    fun reject(orderRejectionDTO: OrderRejectionDTO?)

    fun cancel(orderCancelDTO: OrderCancelDTO?)

    fun delivery(id: Int?)

    fun complete(id: Int?)

    fun reminder(id: Int)
}
