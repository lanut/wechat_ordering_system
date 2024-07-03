package common.niuniu.service

import common.niuniu.vo.BusinessDataVO
import common.niuniu.vo.DishOverViewVO
import common.niuniu.vo.OrderOverViewVO
import common.niuniu.vo.SetmealOverViewVO
import java.time.LocalDateTime

interface WorkSpaceService {
    fun getBusinessData(begin: LocalDateTime, end: LocalDateTime): BusinessDataVO?

    val orderOverView: OrderOverViewVO?

    val dishOverView: DishOverViewVO?

    val setmealOverView: SetmealOverViewVO?
}
