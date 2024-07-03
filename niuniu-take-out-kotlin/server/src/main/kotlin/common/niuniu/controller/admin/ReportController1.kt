package common.niuniu.controller.admin

import common.niuniu.result.Result
import common.niuniu.service.ReportService
import common.niuniu.vo.OrderReportVO
import common.niuniu.vo.SalesTop10ReportVO
import common.niuniu.vo.TurnoverReportVO
import common.niuniu.vo.UserReportVO
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/admin/report")
@Slf4j
class ReportController {
    @Autowired
    private val reportService: ReportService? = null

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    fun turnoverStatistics(
        @DateTimeFormat(pattern = "yyyy-MM-dd") begin: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") end: LocalDate?
    ): Result<TurnoverReportVO> {
        return Result.success(reportService!!.getTurnover(begin, end)!!)
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    fun userStatistics(
        @DateTimeFormat(pattern = "yyyy-MM-dd") begin: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") end: LocalDate?
    ): Result<UserReportVO> {
        return Result.success(reportService!!.getUser(begin, end)!!)
    }

    /**
     * 订单统计（多了 "有效 / 总数 = 订单完成率" 这3个值）
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/orderStatistics")
    fun orderStatistics(
        @DateTimeFormat(pattern = "yyyy-MM-dd") begin: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") end: LocalDate?
    ): Result<OrderReportVO> {
        return Result.success(reportService!!.getOrder(begin, end)!!)
    }

    /**
     * 销量Top10统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10Statistics")
    fun top10Statistics(
        @DateTimeFormat(pattern = "yyyy-MM-dd") begin: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") end: LocalDate?
    ): Result<SalesTop10ReportVO> {
        return Result.success(reportService!!.getSalesTop10(begin, end)!!)
    }

    /**
     * 导出运营数据报表
     * 服务端传给客户端的，客户端不传数据过来
     * @param response
     */
    @GetMapping("/export")
    fun export(response: HttpServletResponse?) {
        reportService!!.exportBusinessData(response)
    }
}
