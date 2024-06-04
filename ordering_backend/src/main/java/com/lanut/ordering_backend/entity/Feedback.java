package com.lanut.ordering_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
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
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Integer feedbackId;

    private Integer userId;

    private Integer dishId;

    private String feedbackContent;

    private LocalDateTime feedbackDate;

    @Override
    public String toString() {
        return "Feedback{" +
        "feedbackId = " + feedbackId +
        ", userId = " + userId +
        ", dishId = " + dishId +
        ", feedbackContent = " + feedbackContent +
        ", feedbackDate = " + feedbackDate +
        "}";
    }
}
