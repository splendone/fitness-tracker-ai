package com.fitness.tracker.service;

import com.fitness.tracker.dto.LoginDTO;
import com.fitness.tracker.entity.User;
import com.fitness.tracker.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 微信登录
     */
    UserVO wxLogin(LoginDTO loginDTO);

    /**
     * 根据ID获取用户
     */
    User getUserById(Long userId);

    /**
     * 根据OpenID获取用户
     */
    User getUserByOpenid(String openid);

    /**
     * 更新用户信息
     */
    boolean updateUser(User user);

    /**
     * 增加经验值并升级
     */
    void addExperience(Long userId, Integer experience);
}
