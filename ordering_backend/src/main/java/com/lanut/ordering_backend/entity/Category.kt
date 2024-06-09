package com.lanut.ordering_backend.entity

import com.baomidou.mybatisplus.annotation.IdType
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
class Category : Serializable {

    @TableId(value = "category_id", type = IdType.AUTO)
    var categoryId: Int? = null

    var categoryName: String? = null

    var description: String? = null

    override fun toString(): String {
        return "Category{" +
        "categoryId=" + categoryId +
        ", categoryName=" + categoryName +
        ", description=" + description +
        "}"
    }
}
