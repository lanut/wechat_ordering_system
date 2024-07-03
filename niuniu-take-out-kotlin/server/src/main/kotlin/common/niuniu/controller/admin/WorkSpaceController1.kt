package common.niuniu.controller.admin

import common.niuniu.annotation.Slf4j
import common.niuniu.result.Result
import common.niuniu.service.WorkSpaceService
import common.niuniu.vo.BusinessDataVO
import common.niuniu.vo.DishOverViewVO
import common.niuniu.vo.OrderOverViewVO
import common.niuniu.vo.SetmealOverViewVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.LocalTime

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
class WorkSpaceController {
    @Autowired
    private val workSpaceService: WorkSpaceService? = null

    /**
     * 工作台今日数据查询
     * @return
     */
    @GetMapping("/businessData")
    fun businessData(): Result<BusinessDataVO> {
        val begin = LocalDateTime.now().with(LocalTime.MIN)
        val end = LocalDateTime.now().with(LocalTime.MAX)
        val businessDataVO = workSpaceService!!.getBusinessData(begin, end)!!
        return Result.success(businessDataVO)
    }

    /**
     * 查询订单管理数据
     * @return
     */
    @GetMapping("/overviewOrders")
    fun orderOverView(): Result<OrderOverViewVO> {
        val orderOverViewVO = workSpaceService!!.orderOverView!!
        return Result.success(orderOverViewVO)
    }

    /**
     * 查询菜品总览
     * @return
     */
    @GetMapping("/overviewDishes")
    fun dishOverView(): Result<DishOverViewVO> {
        val dishOverViewVO = workSpaceService!!.dishOverView!!
        return Result.success(dishOverViewVO)
    }

    /**
     * 查询套餐总览
     * @return
     */
    @GetMapping("/overviewSetmeals")
    fun setmealOverView(): Result<SetmealOverViewVO> {
        val setmealOverViewVO = workSpaceService!!.setmealOverView!!
        return Result.success(setmealOverViewVO)
    }
}
