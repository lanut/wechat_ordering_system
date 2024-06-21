package com.lanut.ordering_backend.entity.dto

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import java.io.Serializable

/**
 * <p>
 * 
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
class Carousel  (
    @TableId(value = "carousel_id", type = IdType.AUTO)
    var carouselId: Int,
    var imageUrl: String,
    var title: String?,
    var link: String?,
    @TableField("`order`")
    var order: Int,
): Serializable {
    override fun toString(): String {
        return "Carousel{" +
        "carouselId=" + carouselId +
        ", imageUrl=" + imageUrl +
        ", title=" + title +
        ", link=" + link +
        ", order=" + order +
        "}"
    }
}
