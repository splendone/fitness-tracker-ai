package com.fitness.tracker.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fitness.tracker.dto.SportRecordDTO;
import com.fitness.tracker.entity.SportRecord;
import com.fitness.tracker.entity.SportType;
import com.fitness.tracker.mapper.SportRecordMapper;
import com.fitness.tracker.mapper.SportTypeMapper;
import com.fitness.tracker.service.SportRecordService;
import com.fitness.tracker.service.UserService;
import com.fitness.tracker.vo.SportStatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 运动记录服务实现
 */
@Service
public class SportRecordServiceImpl implements SportRecordService {

    @Autowired
    private SportRecordMapper sportRecordMapper;

    @Autowired
    private SportTypeMapper sportTypeMapper;

    @Autowired
    private UserService userService;

    @Value("${business.experience.per-minute}")
    private Integer experiencePerMinute;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SportRecord addRecord(SportRecordDTO recordDTO, Long userId) {
        SportRecord record = new SportRecord();
        BeanUtils.copyProperties(recordDTO, record);
        record.setUserId(userId);
        
        // 如果没有提供卡路里，根据运动类型计算
        if (record.getCalories() == null && record.getSportTypeId() != null) {
            SportType sportType = sportTypeMapper.selectById(record.getSportTypeId());
            if (sportType != null && record.getDuration() != null) {
                BigDecimal calories = sportType.getCalorieRate()
                    .multiply(new BigDecimal(record.getDuration()));
                record.setCalories(calories);
            }
        }
        
        sportRecordMapper.insert(record);
        
        // 增加用户经验值
        if (record.getDuration() != null) {
            int experience = record.getDuration() * experiencePerMinute;
            userService.addExperience(userId, experience);
        }
        
        return record;
    }

    @Override
    public Page<SportRecord> getRecordPage(Long userId, Integer current, Integer size) {
        Page<SportRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<SportRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SportRecord::getUserId, userId)
               .orderByDesc(SportRecord::getRecordDate, SportRecord::getCreatedAt);
        return sportRecordMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SportRecord> getRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<SportRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SportRecord::getUserId, userId)
               .between(SportRecord::getRecordDate, startDate, endDate)
               .orderByDesc(SportRecord::getRecordDate);
        return sportRecordMapper.selectList(wrapper);
    }

    @Override
    public SportStatisticsVO getStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> stats = sportRecordMapper.statisticsByDateRange(userId, startDate, endDate);
        
        SportStatisticsVO vo = new SportStatisticsVO();
        vo.setRecordCount(stats.get("recordCount") != null ? 
            Integer.parseInt(stats.get("recordCount").toString()) : 0);
        vo.setTotalDuration(stats.get("totalDuration") != null ? 
            Integer.parseInt(stats.get("totalDuration").toString()) : 0);
        vo.setTotalCalories(stats.get("totalCalories") != null ? 
            new BigDecimal(stats.get("totalCalories").toString()) : BigDecimal.ZERO);
        vo.setTotalDistance(stats.get("totalDistance") != null ? 
            new BigDecimal(stats.get("totalDistance").toString()) : BigDecimal.ZERO);
        
        return vo;
    }

    @Override
    public List<Map<String, Object>> getDailyStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        return sportRecordMapper.statisticsByDay(userId, startDate, endDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long recordId, Long userId) {
        LambdaQueryWrapper<SportRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SportRecord::getId, recordId)
               .eq(SportRecord::getUserId, userId);
        return sportRecordMapper.delete(wrapper) > 0;
    }
}
