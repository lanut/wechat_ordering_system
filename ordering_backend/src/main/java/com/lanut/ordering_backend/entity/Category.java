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
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;

    private String categoryName;

    private String description;

    @Override
    public String toString() {
        return "Category{" +
        "categoryId = " + categoryId +
        ", categoryName = " + categoryName +
        ", description = " + description +
        "}";
    }
}
