package com.lanut.ordering_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "dish_id", type = IdType.AUTO)
    private Integer dishId;

    private Integer categoryId;

    private String dishName;

    private BigDecimal price;

    private String description;

    private String imageUrl;

    private String status;

    @Override
    public String toString() {
        return "Dish{" +
        "dishId = " + dishId +
        ", categoryId = " + categoryId +
        ", dishName = " + dishName +
        ", price = " + price +
        ", description = " + description +
        ", imageUrl = " + imageUrl +
        ", status = " + status +
        "}";
    }
}
