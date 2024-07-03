package common.niuniu.service

import common.niuniu.vo.OrderReportVO
import common.niuniu.vo.SalesTop10ReportVO
import common.niuniu.vo.TurnoverReportVO
import common.niuniu.vo.UserReportVO
import jakarta.servlet.http.HttpServletResponse
import java.time.LocalDate

interface ReportService {
    fun getTurnover(begin: LocalDate?, end: LocalDate?): TurnoverReportVO?

    fun getUser(begin: LocalDate?, end: LocalDate?): UserReportVO?

    fun getOrder(begin: LocalDate?, end: LocalDate?): OrderReportVO?

    fun getSalesTop10(begin: LocalDate?, end: LocalDate?): SalesTop10ReportVO?

    fun exportBusinessData(response: HttpServletResponse?)
}
