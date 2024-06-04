package com.lanut.ordering_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Data
@TableName("order_detail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_detail_id", type = IdType.AUTO)
    private Integer orderDetailId;

    private Integer orderId;

    private Integer dishId;

    private Integer quantity;

    private BigDecimal subtotal;

    @Override
    public String toString() {
        return "OrderDetail{" +
        "orderDetailId = " + orderDetailId +
        ", orderId = " + orderId +
        ", dishId = " + dishId +
        ", quantity = " + quantity +
        ", subtotal = " + subtotal +
        "}";
    }
}
