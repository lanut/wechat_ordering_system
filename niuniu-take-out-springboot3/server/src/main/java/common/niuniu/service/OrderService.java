package common.niuniu.service;

import common.niuniu.dto.*;
import common.niuniu.result.PageResult;
import common.niuniu.vo.OrderPaymentVO;
import common.niuniu.vo.OrderStatisticsVO;
import common.niuniu.vo.OrderSubmitVO;
import common.niuniu.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);

    Integer unPayOrderCount();

    OrderVO getById(Integer id);

    PageResult userPage(int page, int pageSize, Integer status);

    void userCancelById(Integer id) throws Exception;

    void reOrder(Integer id);

    OrderPaymentVO payment(OrderPaymentDTO orderPaymentDTO);

    PageResult conditionSearch(OrderPageDTO orderPageDTO);

    OrderStatisticsVO statistics();

    void confirm(OrderConfirmDTO orderConfirmDTO);

    void reject(OrderRejectionDTO orderRejectionDTO);

    void cancel(OrderCancelDTO orderCancelDTO);

    void delivery(Integer id);

    void complete(Integer id);

    void reminder(Integer id);
}
