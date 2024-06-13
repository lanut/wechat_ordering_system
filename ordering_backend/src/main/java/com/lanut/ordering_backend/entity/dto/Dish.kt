package com.lanut.ordering_backend.entity.dto

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import java.io.Serializable
import java.math.BigDecimal

/**
 * <p>
 *
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
class Dish(
    @TableId(value = "dish_id", type = IdType.AUTO) var dishId: Int,
    var categoryId: Int,
    var dishName: String,
    var price: BigDecimal,
    var description: String?,
    var imageUrl: String?,
    var status: String,
) : Serializable {
    override fun toString(): String {
        return "Dish{" +
                "dishId=" + dishId +
                ", categoryId=" + categoryId +
                ", dishName=" + dishName +
                ", price=" + price +
                ", description=" + description +
                ", imageUrl=" + imageUrl +
                ", status=" + status +
                "}"
    }
}
