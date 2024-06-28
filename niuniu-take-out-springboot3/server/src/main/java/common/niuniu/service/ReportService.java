package common.niuniu.service;

import common.niuniu.vo.OrderReportVO;
import common.niuniu.vo.SalesTop10ReportVO;
import common.niuniu.vo.TurnoverReportVO;
import common.niuniu.vo.UserReportVO;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO getTurnover(LocalDate begin, LocalDate end);

    UserReportVO getUser(LocalDate begin, LocalDate end);

    OrderReportVO getOrder(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response);
}
