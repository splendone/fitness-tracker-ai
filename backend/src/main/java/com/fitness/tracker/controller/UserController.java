package com.fitness.tracker.controller;

import com.fitness.tracker.common.Result;
import com.fitness.tracker.dto.LoginDTO;
import com.fitness.tracker.entity.User;
import com.fitness.tracker.service.UserService;
import com.fitness.tracker.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 微信登录
     */
    @PostMapping("/login")
    public Result<UserVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        UserVO userVO = userService.wxLogin(loginDTO);
        return Result.success(userVO);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info/{userId}")
    public Result<User> getUserInfo(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<Void> updateUser(@RequestBody User user) {
        boolean success = userService.updateUser(user);
        return success ? Result.success() : Result.error("更新失败");
    }
}
