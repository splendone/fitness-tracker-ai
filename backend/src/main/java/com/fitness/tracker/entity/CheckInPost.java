package com.fitness.tracker.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 打卡动态实体
 */
@Data
@TableName("check_in_post")
public class CheckInPost implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long sportRecordId;

    private String content;

    private String images;

    private String location;

    private Integer visibility;

    private Integer likeCount;

    private Integer commentCount;

    private Integer shareCount;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
