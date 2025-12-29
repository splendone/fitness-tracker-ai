package com.fitness.tracker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fitness.tracker.entity.SportType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 运动类型Mapper
 */
@Mapper
public interface SportTypeMapper extends BaseMapper<SportType> {
}
