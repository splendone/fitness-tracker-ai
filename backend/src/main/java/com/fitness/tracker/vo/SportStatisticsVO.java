package com.fitness.tracker.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 运动统计VO
 */
@Data
public class SportStatisticsVO {
    
    private Integer recordCount;
    
    private Integer totalDuration;
    
    private BigDecimal totalCalories;
    
    private BigDecimal totalDistance;
    
    private BigDecimal avgCaloriesPerDay;
    
    private BigDecimal avgDurationPerDay;
}
