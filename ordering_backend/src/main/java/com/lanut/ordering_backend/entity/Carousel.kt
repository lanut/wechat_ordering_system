package com.lanut.ordering_backend.entity

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
class Carousel : Serializable {

    @TableId(value = "carousel_id", type = IdType.AUTO)
    var carouselId: Int? = null

    var imageUrl: String? = null

    var title: String? = null

    var link: String? = null

    @TableField("`order`")
    var order: Int? = null

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
