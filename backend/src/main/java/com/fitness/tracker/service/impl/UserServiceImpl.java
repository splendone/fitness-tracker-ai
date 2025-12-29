package com.fitness.tracker.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fitness.tracker.dto.LoginDTO;
import com.fitness.tracker.entity.User;
import com.fitness.tracker.mapper.UserMapper;
import com.fitness.tracker.service.UserService;
import com.fitness.tracker.utils.JwtUtil;
import com.fitness.tracker.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${wechat.miniapp.appid}")
    private String appid;

    @Value("${wechat.miniapp.secret}")
    private String secret;

    @Value("${business.experience.per-minute}")
    private Integer experiencePerMinute;

    @Value("${business.experience.level-up-base}")
    private Integer levelUpBase;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO wxLogin(LoginDTO loginDTO) {
        // 调用微信接口获取openid
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            appid, secret, loginDTO.getCode()
        );
        
        String result = HttpUtil.get(url);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        
        if (jsonObject.containsKey("errcode")) {
            throw new RuntimeException("微信登录失败: " + jsonObject.getStr("errmsg"));
        }
        
        String openid = jsonObject.getStr("openid");
        String unionid = jsonObject.getStr("unionid");
        
        // 查询用户是否存在
        User user = getUserByOpenid(openid);
        
        if (user == null) {
            // 新用户注册
            user = new User();
            user.setOpenid(openid);
            user.setUnionid(unionid);
            user.setNickname(loginDTO.getNickname());
            user.setAvatar(loginDTO.getAvatar());
            user.setGender(loginDTO.getGender());
            user.setLevel(1);
            user.setExperience(0);
            user.setTotalDays(0);
            user.setContinuousDays(0);
            user.setStatus(1);
            userMapper.insert(user);
        } else {
            // 更新用户信息
            if (loginDTO.getNickname() != null) {
                user.setNickname(loginDTO.getNickname());
            }
            if (loginDTO.getAvatar() != null) {
                user.setAvatar(loginDTO.getAvatar());
            }
            userMapper.updateById(user);
        }
        
        // 生成token
        String token = jwtUtil.generateToken(user.getId(), openid);
        
        // 构建返回VO
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setToken(token);
        
        return userVO;
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User getUserByOpenid(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateById(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addExperience(Long userId, Integer experience) {
        User user = getUserById(userId);
        if (user == null) {
            return;
        }
        
        int newExperience = user.getExperience() + experience;
        int currentLevel = user.getLevel();
        
        // 计算升级所需经验值
        int expNeeded = levelUpBase * currentLevel;
        
        // 判断是否升级
        while (newExperience >= expNeeded) {
            currentLevel++;
            newExperience -= expNeeded;
            expNeeded = levelUpBase * currentLevel;
        }
        
        user.setLevel(currentLevel);
        user.setExperience(newExperience);
        userMapper.updateById(user);
    }
}
