package com.fitness.tracker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fitness.tracker.entity.SportRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 运动记录Mapper
 */
@Mapper
public interface SportRecordMapper extends BaseMapper<SportRecord> {

    /**
     * 统计用户指定日期范围的运动数据
     */
    @Select("SELECT " +
            "COUNT(*) as recordCount, " +
            "SUM(duration) as totalDuration, " +
            "SUM(calories) as totalCalories, " +
            "SUM(distance) as totalDistance " +
            "FROM sport_record " +
            "WHERE user_id = #{userId} " +
            "AND record_date >= #{startDate} " +
            "AND record_date <= #{endDate}")
    Map<String, Object> statisticsByDateRange(@Param("userId") Long userId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    /**
     * 按日期分组统计
     */
    @Select("SELECT " +
            "record_date as date, " +
            "SUM(duration) as duration, " +
            "SUM(calories) as calories, " +
            "SUM(distance) as distance " +
            "FROM sport_record " +
            "WHERE user_id = #{userId} " +
            "AND record_date >= #{startDate} " +
            "AND record_date <= #{endDate} " +
            "GROUP BY record_date " +
            "ORDER BY record_date")
    List<Map<String, Object>> statisticsByDay(@Param("userId") Long userId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
}
