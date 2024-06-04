package com.lanut.ordering_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    private Integer userId;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    private String orderStatus;

    @Override
    public String toString() {
        return "Order{" +
        "orderId = " + orderId +
        ", userId = " + userId +
        ", orderDate = " + orderDate +
        ", totalAmount = " + totalAmount +
        ", orderStatus = " + orderStatus +
        "}";
    }
}
