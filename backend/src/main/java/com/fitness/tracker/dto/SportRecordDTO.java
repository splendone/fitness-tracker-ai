package com.fitness.tracker.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 运动记录DTO
 */
@Data
public class SportRecordDTO {
    
    @NotNull(message = "运动类型不能为空")
    private Integer sportTypeId;
    
    @NotNull(message = "运动时长不能为空")
    private Integer duration;
    
    private BigDecimal distance;
    
    private BigDecimal calories;
    
    private Integer steps;
    
    private Integer heartRateAvg;
    
    private Integer heartRateMax;
    
    private BigDecimal speedAvg;
    
    @NotNull(message = "运动日期不能为空")
    private LocalDate recordDate;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private String note;
    
    private String images;
    
    private String syncSource;
}
