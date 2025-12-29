package com.fitness.tracker.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 运动记录实体
 */
@Data
@TableName("sport_record")
public class SportRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer sportTypeId;

    private Integer duration;

    private BigDecimal distance;

    private BigDecimal calories;

    private Integer steps;

    private Integer heartRateAvg;

    private Integer heartRateMax;

    private BigDecimal speedAvg;

    private LocalDate recordDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String note;

    private String images;

    private String syncSource;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
