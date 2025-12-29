package com.fitness.tracker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fitness.tracker.entity.CheckInPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 打卡动态Mapper
 */
@Mapper
public interface CheckInPostMapper extends BaseMapper<CheckInPost> {
}
