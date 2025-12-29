package com.fitness.tracker.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fitness.tracker.dto.SportRecordDTO;
import com.fitness.tracker.entity.SportRecord;
import com.fitness.tracker.vo.SportStatisticsVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 运动记录服务接口
 */
public interface SportRecordService {

    /**
     * 添加运动记录
     */
    SportRecord addRecord(SportRecordDTO recordDTO, Long userId);

    /**
     * 分页查询运动记录
     */
    Page<SportRecord> getRecordPage(Long userId, Integer current, Integer size);

    /**
     * 根据日期范围查询
     */
    List<SportRecord> getRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 统计数据
     */
    SportStatisticsVO getStatistics(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 按日期分组统计
     */
    List<Map<String, Object>> getDailyStatistics(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 删除记录
     */
    boolean deleteRecord(Long recordId, Long userId);
}
