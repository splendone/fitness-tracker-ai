package com.fitness.tracker.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fitness.tracker.common.Result;
import com.fitness.tracker.dto.SportRecordDTO;
import com.fitness.tracker.entity.SportRecord;
import com.fitness.tracker.service.SportRecordService;
import com.fitness.tracker.vo.SportStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 运动记录控制器
 */
@RestController
@RequestMapping("/sport/record")
public class SportRecordController {

    @Autowired
    private SportRecordService sportRecordService;

    /**
     * 添加运动记录
     */
    @PostMapping("/add")
    public Result<SportRecord> addRecord(@Validated @RequestBody SportRecordDTO recordDTO,
                                          @RequestHeader("userId") Long userId) {
        SportRecord record = sportRecordService.addRecord(recordDTO, userId);
        return Result.success(record);
    }

    /**
     * 分页查询运动记录
     */
    @GetMapping("/page")
    public Result<Page<SportRecord>> getRecordPage(@RequestHeader("userId") Long userId,
                                                     @RequestParam(defaultValue = "1") Integer current,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        Page<SportRecord> page = sportRecordService.getRecordPage(userId, current, size);
        return Result.success(page);
    }

    /**
     * 按日期范围查询
     */
    @GetMapping("/list")
    public Result<List<SportRecord>> getRecordsByDateRange(
            @RequestHeader("userId") Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<SportRecord> records = sportRecordService.getRecordsByDateRange(userId, startDate, endDate);
        return Result.success(records);
    }

    /**
     * 统计数据
     */
    @GetMapping("/statistics")
    public Result<SportStatisticsVO> getStatistics(
            @RequestHeader("userId") Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        SportStatisticsVO statistics = sportRecordService.getStatistics(userId, startDate, endDate);
        return Result.success(statistics);
    }

    /**
     * 按日统计
     */
    @GetMapping("/daily-statistics")
    public Result<List<Map<String, Object>>> getDailyStatistics(
            @RequestHeader("userId") Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Map<String, Object>> statistics = sportRecordService.getDailyStatistics(userId, startDate, endDate);
        return Result.success(statistics);
    }

    /**
     * 删除记录
     */
    @DeleteMapping("/delete/{recordId}")
    public Result<Void> deleteRecord(@PathVariable Long recordId,
                                      @RequestHeader("userId") Long userId) {
        boolean success = sportRecordService.deleteRecord(recordId, userId);
        return success ? Result.success() : Result.error("删除失败");
    }
}
