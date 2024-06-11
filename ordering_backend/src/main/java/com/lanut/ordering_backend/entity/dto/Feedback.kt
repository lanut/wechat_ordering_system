package com.lanut.ordering_backend.entity.dto

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import java.io.Serializable
import java.time.LocalDateTime

/**
 * <p>
 * 
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
class Feedback : Serializable {

    @TableId(value = "feedback_id", type = IdType.AUTO)
    var feedbackId: Int? = null

    var userId: Int? = null

    var orderId: Int? = null

    var feedbackContent: String? = null

    var feedbackDate: LocalDateTime? = null

    var rating: Int? = null // 1-5

    override fun toString(): String {
        return "Feedback{" +
        "feedbackId=" + feedbackId +
        ", userId=" + userId +
        ", dishId=" + orderId +
        ", feedbackContent=" + feedbackContent +
        ", feedbackDate=" + feedbackDate +
        ", rating=" + rating +
        "}"
    }
}
