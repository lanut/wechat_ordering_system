package common.niuniu.vo;

import common.niuniu.po.Order;
import common.niuniu.po.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Order implements Serializable {

    private String orderDishes; // 订单菜品信息
    private List<OrderDetail> orderDetailList; // 订单详情

}
