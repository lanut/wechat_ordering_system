package com.lanut.ordering_backend.entity

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
class Dish : Serializable {

    @TableId(value = "dish_id", type = IdType.AUTO)
    var dishId: Int? = null

    var categoryId: Int? = null

    var dishName: String? = null

    var price: BigDecimal? = null

    var description: String? = null

    var imageUrl: String? = null

    var status: String? = null

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
