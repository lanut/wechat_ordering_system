package com.lanut.ordering_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lanut
 * @since 2024-06-04
 */
@Data
public class Carousel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "carousel_id", type = IdType.AUTO)
    private Integer carouselId;

    private String imageUrl;

    private String title;

    private String link;

    private Integer order;

    @Override
    public String toString() {
        return "Carousel{" +
        "carouselId = " + carouselId +
        ", imageUrl = " + imageUrl +
        ", title = " + title +
        ", link = " + link +
        ", order = " + order +
        "}";
    }
}
