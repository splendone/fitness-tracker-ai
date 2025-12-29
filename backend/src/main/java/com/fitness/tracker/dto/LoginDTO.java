package com.fitness.tracker.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录DTO
 */
@Data
public class LoginDTO {
    
    @NotBlank(message = "code不能为空")
    private String code;
    
    private String nickname;
    
    private String avatar;
    
    private Integer gender;
}
