package common.niuniu.mapper

import com.github.pagehelper.Page
import common.niuniu.dto.GoodsSalesDTO
import common.niuniu.dto.OrderPageDTO
import common.niuniu.po.Order
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import java.time.LocalDateTime

@Mapper
interface OrderMapper {
    fun insert(order: Order?)

    @Select("select * from orders where id = #{id}")
    fun getById(id: Int?): Order?

    fun page(orderPageDTO: OrderPageDTO?): Page<Order?>?

    fun update(order: Order?)

    /**
     * 修改订单状态，支付状态，结账时间
     * @param orderStatus
     * @param orderPaidStatus
     * @param checkOutTime
     * @param id
     */
    @Update(
        "update orders set status = #{orderStatus}, pay_status = #{orderPaidStatus}, checkout_time = #{checkOutTime} " +
                "where id = #{id}"
    )
    fun updateStatus(orderStatus: Int?, orderPaidStatus: Int?, checkOutTime: LocalDateTime?, id: Int?)

    @Select("select count(id) from orders where user_id = #{userId} and status = 1")
    fun getUnPayCount(userId: Int?): Int?

    @Select("select count(id) from orders where status = #{status}")
    fun countByStatus(status: Int?): Int?

    /**
     * 根据状态和下单时间查询订单
     * @param status
     * @param orderTime
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    fun getByStatusAndOrderTimeLT(status: Int?, orderTime: LocalDateTime?): List<Order?>?

    fun sumByMap(map: Map<*, *>?): Double?

    fun countByMap(map: Map<*, *>?): Int

    fun getSalesTop10(beginTime: LocalDateTime?, endTime: LocalDateTime?): List<GoodsSalesDTO?>?
}
