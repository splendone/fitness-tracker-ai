package com.fitness.tracker.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 用户VO
 */
@Data
public class UserVO {
    
    private Long id;
    
    private String openid;
    
    private String nickname;
    
    private String avatar;
    
    private Integer gender;
    
    private Integer age;
    
    private BigDecimal height;
    
    private BigDecimal weight;
    
    private BigDecimal targetWeight;
    
    private String phone;
    
    private Integer level;
    
    private Integer experience;
    
    private Integer totalDays;
    
    private Integer continuousDays;
    
    private String token;
}
