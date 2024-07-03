package common.niuniu.service.serviceImpl

import common.niuniu.dto.GoodsSalesDTO
import common.niuniu.mapper.OrderMapper
import common.niuniu.mapper.UserMapper
import common.niuniu.po.Order
import common.niuniu.service.ReportService
import common.niuniu.service.WorkSpaceService
import common.niuniu.vo.OrderReportVO
import common.niuniu.vo.SalesTop10ReportVO
import common.niuniu.vo.TurnoverReportVO
import common.niuniu.vo.UserReportVO
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.stream.Collectors

@Service
class ReportServiceImpl : ReportService {
    @Autowired
    private lateinit var orderMapper: OrderMapper

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var workSpaceService: WorkSpaceService

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    override fun getTurnover(begin: LocalDate?, end: LocalDate?): TurnoverReportVO? {
        var begin = begin
        val dateList: MutableList<LocalDate?> = ArrayList()
        dateList.add(begin)
        while (begin != end) {
            begin = begin!!.plusDays(1) // 日期计算，获得指定日期后1天的日期
            dateList.add(begin)
        }
        val turnoverList: MutableList<Double?> = ArrayList()
        for (date in dateList) {
            // 每一天的最早和最晚时刻，作为 begin 和 end
            val beginTime = LocalDateTime.of(date, LocalTime.MIN)
            val endTime = LocalDateTime.of(date, LocalTime.MAX)
            val map: MutableMap<String, Any> = mutableMapOf()
            map["status"] = Order.COMPLETED
            map["begin"] = beginTime
            map["end"] = endTime
            // 统计当天的营业额，并加到 turnoverList 里，null要转为0
            var turnover = orderMapper.sumByMap(map)
            turnover = turnover ?: 0.0
            turnoverList.add(turnover)
        }
        //数据封装
        return TurnoverReportVO.builder()
            .dateList(StringUtils.join(dateList, ","))
            .turnoverList(StringUtils.join(turnoverList, ","))
            .build()
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    override fun getUser(begin: LocalDate?, end: LocalDate?): UserReportVO? {
        var begin = begin
        val dateList: MutableList<LocalDate?> = ArrayList()
        dateList.add(begin)
        while (begin != end) {
            begin = begin!!.plusDays(1) // 日期计算，获得指定日期后1天的日期
            dateList.add(begin)
        }
        val newUserList: MutableList<Int?> = ArrayList()
        val totalUserList: MutableList<Int?> = ArrayList()
        for (date in dateList) {
            // 每一天的最早和最晚时刻，作为 begin 和 end
            val beginTime = LocalDateTime.of(date, LocalTime.MIN)
            val endTime = LocalDateTime.of(date, LocalTime.MAX)
            val map: MutableMap<String, Any> = mutableMapOf()
            // 先统计截至当天总用户数
            map["end"] = endTime
            val totalUser = userMapper.countByMap(map)
            // 再加上 begin 条件，统计当天的新增用户数和
            map["begin"] = beginTime
            val newUser = userMapper.countByMap(map)
            newUserList.add(newUser)
            totalUserList.add(totalUser)
        }
        // 数据封装
        return UserReportVO.builder()
            .dateList(StringUtils.join(dateList, ","))
            .newUserList(StringUtils.join(newUserList, ","))
            .totalUserList(StringUtils.join(totalUserList, ","))
            .build()
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    override fun getOrder(begin: LocalDate?, end: LocalDate?): OrderReportVO? {
        var begin = begin
        val dateList: MutableList<LocalDate?> = ArrayList()
        dateList.add(begin)
        while (begin != end) {
            begin = begin!!.plusDays(1) // 日期计算，获得指定日期后1天的日期
            dateList.add(begin)
        }
        val orderCountList: MutableList<Int> = mutableListOf()
        val validOrderCountList: MutableList<Int> = mutableListOf()
        for (date in dateList) {
            // 每一天的最早和最晚时刻，作为 begin 和 end
            val beginTime = LocalDateTime.of(date, LocalTime.MIN)
            val endTime = LocalDateTime.of(date, LocalTime.MAX)
            val map: MutableMap<String, Any> = mutableMapOf()
            // 先统计当天总订单数
            map["begin"] = beginTime
            map["end"] = endTime
            val orderCount = orderMapper.countByMap(map)
            // 再加上 status 条件，统计当天有效订单数
            map["status"] = Order.COMPLETED
            val validCount = orderMapper.countByMap(map)
            orderCountList.add(orderCount)
            validOrderCountList.add(validCount)
        }
        // 流式计算这段时间内的有效订单数和订单总数，相除再得出订单完成率
        // 1、时间区间内的总订单数
        val totalOrderCount =
            orderCountList.stream().reduce { a: Int, b: Int -> Integer.sum(a, b) }.get()
        // 2、时间区间内的总有效订单数
        val validOrderCount =
            validOrderCountList.stream().reduce { a: Int, b: Int -> Integer.sum(a, b) }.get()
        // 3、订单完成率
        var orderCompletionRate = 0.0
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.toDouble() / totalOrderCount
        }
        return OrderReportVO.builder()
            .dateList(StringUtils.join(dateList, ","))
            .orderCountList(StringUtils.join(orderCountList, ","))
            .validOrderCountList(StringUtils.join(validOrderCountList, ","))
            .totalOrderCount(totalOrderCount)
            .validOrderCount(validOrderCount)
            .orderCompletionRate(orderCompletionRate)
            .build()
    }

    /**
     * 销量Top10统计
     * @param begin
     * @param end
     * @return
     */
    override fun getSalesTop10(begin: LocalDate?, end: LocalDate?): SalesTop10ReportVO? {
        val beginTime = LocalDateTime.of(begin, LocalTime.MIN)
        val endTime = LocalDateTime.of(end, LocalTime.MAX)
        // Top 10 列表，每个元素都有商品名称和销量值
        val goodsSalesDTOList = orderMapper.getSalesTop10(beginTime, endTime)
        // 流式操作，拿到列表中所有name/number属性组成的列表，然后再用","序列化成字符串，变成xxx,xxx,xxx的形式
        val nameList = StringUtils.join(
            goodsSalesDTOList!!.stream()
                .map { obj: GoodsSalesDTO? -> obj!!.name }.collect(Collectors.toList()), ","
        )
        val numberList = StringUtils.join(
            goodsSalesDTOList.stream()
                .map { obj: GoodsSalesDTO? -> obj!!.number }.collect(Collectors.toList()), ","
        )
        // 数据封装返回
        return SalesTop10ReportVO.builder()
            .nameList(nameList)
            .numberList(numberList)
            .build()
    }

    /**
     * 导出近30天的运营数据报表
     * @param response
     */
    override fun exportBusinessData(response: HttpServletResponse?) {
        // 提前将资料中的 运营数据报表模板.xlsx 拷贝到项目的resources/template目录中
        // 拿到 前30天 - 前1天 的数据
        val begin = LocalDate.now().minusDays(30)
        // 日期 转 日期加时间，转的时候要指定时间字段
        val beginTime = LocalDateTime.of(begin, LocalTime.MIN)
        val end = LocalDate.now().minusDays(1)
        val endTime = LocalDateTime.of(end, LocalTime.MAX)
        // 调用service方法来获取工作台数据（注意是service而不是mapper，因为这个功能之前实现过，直接拿来用就行）
        var businessData = workSpaceService.getBusinessData(beginTime, endTime)
        val `in` = this.javaClass.classLoader.getResourceAsStream("template/运营数据报表模板.xlsx")
        try {
            val excel = XSSFWorkbook(`in`)
            val sheet = excel.getSheetAt(0)
            // 第2行写入时间字段
            sheet.getRow(1).getCell(1).setCellValue(begin.toString() + "至" + end)
            // 第4、5行写入概览数据
            val row4 = sheet.getRow(3)
            // 获取单元格，填入营业额、订单完成率、新增用户数量
            row4.getCell(2).setCellValue(businessData!!.turnover)
            row4.getCell(4).setCellValue(businessData.orderCompletionRate)
            row4.getCell(6).setCellValue(businessData.newUsers.toDouble())
            val row5 = sheet.getRow(4)
            // 获取单元格，填入有效订单数、订单平均价格
            row5.getCell(2).setCellValue(businessData.validOrderCount.toDouble())
            row5.getCell(4).setCellValue(businessData.unitPrice)
            // 插入30行明细数据，每行6个单元格的值对应一天的数据概览
            for (i in 0..29) {
                val date = begin.plusDays(i.toLong())
                // 准备每天的明细数据
                businessData = workSpaceService.getBusinessData(
                    LocalDateTime.of(date, LocalTime.MIN),
                    LocalDateTime.of(date, LocalTime.MAX)
                )
                val row = sheet.getRow(7 + i)
                row.getCell(1).setCellValue(date.toString())
                row.getCell(2).setCellValue(businessData!!.turnover)
                row.getCell(3).setCellValue(businessData.validOrderCount.toDouble())
                row.getCell(4).setCellValue(businessData.orderCompletionRate)
                row.getCell(5).setCellValue(businessData.unitPrice)
                row.getCell(6).setCellValue(businessData.newUsers.toDouble())
            }
            // 创建输出流，excel数据放进流里，通过输出流将文件下载到客户端浏览器中
            val out = response!!.outputStream
            excel.write(out)
            // 关闭资源
            out.flush()
            out.close()
            excel.close()
        } catch (e: IOException) {
            // 打印错误就行，不要抛异常使程序中断
            e.printStackTrace()
        }
    }
}
