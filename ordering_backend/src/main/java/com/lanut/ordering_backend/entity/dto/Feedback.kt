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
class Feedback(
    @TableId(value = "feedback_id", type = IdType.AUTO)
    var feedbackId: Int,
    var userId: Int,
    var orderId: Int,
    var feedbackContent: String,
    var feedbackDate: LocalDateTime,
    var rating: Int?
) : Serializable {
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
