package com.fitness.tracker.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 健身计划实体
 */
@Data
@TableName("fitness_plan")
public class FitnessPlan implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String name;

    private String goal;

    private String level;

    private Integer durationWeeks;

    private Integer frequencyPerWeek;

    private String planContent;

    private Integer aiGenerated;

    private String aiModel;

    private Integer status;

    private LocalDate startDate;

    private LocalDate endDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
