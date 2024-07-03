package common.niuniu.mapper

import common.niuniu.po.OrderDetail
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface OrderDetailMapper {
    fun insertBatch(orderDetailList: List<OrderDetail?>?)

    @Select("select * from order_detail where order_id = #{id}")
    fun getById(id: Int?): List<OrderDetail?>?
}
