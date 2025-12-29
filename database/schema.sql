-- ============================================
-- 健身数据追踪小程序 - 数据库设计
-- Version: 1.0
-- ============================================

CREATE DATABASE IF NOT EXISTS fitness_tracker DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fitness_tracker;

-- ============================================
-- 用户表
-- ============================================
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `openid` VARCHAR(100) UNIQUE NOT NULL COMMENT '微信OpenID',
    `unionid` VARCHAR(100) COMMENT '微信UnionID',
    `nickname` VARCHAR(100) COMMENT '昵称',
    `avatar` VARCHAR(500) COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `age` INT COMMENT '年龄',
    `height` DECIMAL(5,2) COMMENT '身高(cm)',
    `weight` DECIMAL(5,2) COMMENT '体重(kg)',
    `target_weight` DECIMAL(5,2) COMMENT '目标体重(kg)',
    `phone` VARCHAR(20) COMMENT '手机号',
    `level` INT DEFAULT 1 COMMENT '等级',
    `experience` INT DEFAULT 0 COMMENT '经验值',
    `total_days` INT DEFAULT 0 COMMENT '累计打卡天数',
    `continuous_days` INT DEFAULT 0 COMMENT '连续打卡天数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_openid` (`openid`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 运动类型表
-- ============================================
CREATE TABLE `sport_type` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '运动类型ID',
    `name` VARCHAR(50) NOT NULL COMMENT '运动名称',
    `category` VARCHAR(20) NOT NULL COMMENT '分类：aerobic-有氧，strength-力量，flexibility-柔韧',
    `calorie_rate` DECIMAL(5,2) DEFAULT 5.0 COMMENT '卡路里消耗率(卡/分钟)',
    `icon` VARCHAR(200) COMMENT '图标URL',
    `description` TEXT COMMENT '描述',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动类型表';

-- ============================================
-- 运动记录表
-- ============================================
CREATE TABLE `sport_record` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `sport_type_id` INT NOT NULL COMMENT '运动类型ID',
    `duration` INT NOT NULL COMMENT '运动时长(分钟)',
    `distance` DECIMAL(10,2) COMMENT '距离(公里)',
    `calories` DECIMAL(10,2) COMMENT '消耗卡路里',
    `steps` INT COMMENT '步数',
    `heart_rate_avg` INT COMMENT '平均心率',
    `heart_rate_max` INT COMMENT '最大心率',
    `speed_avg` DECIMAL(5,2) COMMENT '平均速度(km/h)',
    `record_date` DATE NOT NULL COMMENT '运动日期',
    `start_time` DATETIME COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `note` TEXT COMMENT '备注',
    `images` JSON COMMENT '图片URLs',
    `sync_source` VARCHAR(20) DEFAULT 'manual' COMMENT '数据来源：manual-手动，wechat-微信运动，other-其他',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_date` (`user_id`, `record_date`),
    INDEX `idx_user_sport` (`user_id`, `sport_type_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`sport_type_id`) REFERENCES `sport_type`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动记录表';

-- ============================================
-- 体重记录表
-- ============================================
CREATE TABLE `weight_record` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `weight` DECIMAL(5,2) NOT NULL COMMENT '体重(kg)',
    `bmi` DECIMAL(5,2) COMMENT 'BMI指数',
    `body_fat_rate` DECIMAL(5,2) COMMENT '体脂率(%)',
    `muscle_rate` DECIMAL(5,2) COMMENT '肌肉率(%)',
    `record_date` DATE NOT NULL COMMENT '记录日期',
    `note` TEXT COMMENT '备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_date` (`user_id`, `record_date`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    UNIQUE KEY `uk_user_date` (`user_id`, `record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='体重记录表';

-- ============================================
-- 健身计划表
-- ============================================
CREATE TABLE `fitness_plan` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '计划ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `name` VARCHAR(100) NOT NULL COMMENT '计划名称',
    `goal` VARCHAR(50) COMMENT '目标：lose_weight-减重，gain_muscle-增肌，keep_fit-保持',
    `level` VARCHAR(20) COMMENT '难度：beginner-初级，intermediate-中级，advanced-高级',
    `duration_weeks` INT COMMENT '周期(周)',
    `frequency_per_week` INT COMMENT '每周训练次数',
    `plan_content` JSON COMMENT '计划详情(JSON格式)',
    `ai_generated` TINYINT DEFAULT 0 COMMENT '是否AI生成：0-否，1-是',
    `ai_model` VARCHAR(50) COMMENT 'AI模型名称',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-已结束，1-进行中，2-已暂停',
    `start_date` DATE COMMENT '开始日期',
    `end_date` DATE COMMENT '结束日期',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_status` (`user_id`, `status`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健身计划表';

-- ============================================
-- 计划执行记录表
-- ============================================
CREATE TABLE `plan_execution` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '执行记录ID',
    `plan_id` BIGINT NOT NULL COMMENT '计划ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `sport_record_id` BIGINT COMMENT '关联运动记录ID',
    `scheduled_date` DATE NOT NULL COMMENT '计划日期',
    `completed` TINYINT DEFAULT 0 COMMENT '是否完成：0-未完成，1-已完成',
    `completion_rate` DECIMAL(5,2) COMMENT '完成度(%)',
    `feedback` TEXT COMMENT '反馈',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_plan_date` (`plan_id`, `scheduled_date`),
    INDEX `idx_user_date` (`user_id`, `scheduled_date`),
    FOREIGN KEY (`plan_id`) REFERENCES `fitness_plan`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`sport_record_id`) REFERENCES `sport_record`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计划执行记录表';

-- ============================================
-- 打卡动态表
-- ============================================
CREATE TABLE `check_in_post` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '动态ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `sport_record_id` BIGINT COMMENT '关联运动记录ID',
    `content` TEXT COMMENT '动态内容',
    `images` JSON COMMENT '图片URLs',
    `location` VARCHAR(200) COMMENT '位置',
    `visibility` TINYINT DEFAULT 1 COMMENT '可见性：0-私密，1-公开',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `share_count` INT DEFAULT 0 COMMENT '分享数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-已删除，1-正常',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_status` (`user_id`, `status`),
    INDEX `idx_created` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`sport_record_id`) REFERENCES `sport_record`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡动态表';

-- ============================================
-- 点赞表
-- ============================================
CREATE TABLE `post_like` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
    `post_id` BIGINT NOT NULL COMMENT '动态ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_post_user` (`post_id`, `user_id`),
    INDEX `idx_user` (`user_id`),
    FOREIGN KEY (`post_id`) REFERENCES `check_in_post`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- ============================================
-- 评论表
-- ============================================
CREATE TABLE `post_comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    `post_id` BIGINT NOT NULL COMMENT '动态ID',
    `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
    `parent_id` BIGINT COMMENT '父评论ID(用于回复)',
    `reply_to_user_id` BIGINT COMMENT '回复目标用户ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-已删除，1-正常',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_post_status` (`post_id`, `status`),
    INDEX `idx_user` (`user_id`),
    INDEX `idx_parent` (`parent_id`),
    FOREIGN KEY (`post_id`) REFERENCES `check_in_post`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`parent_id`) REFERENCES `post_comment`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ============================================
-- 排行榜统计表
-- ============================================
CREATE TABLE `leaderboard` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '排行ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `type` VARCHAR(20) NOT NULL COMMENT '排行类型：daily-日，weekly-周，monthly-月',
    `period` VARCHAR(20) NOT NULL COMMENT '周期标识：如2024-01-01，2024-W01',
    `total_calories` DECIMAL(10,2) DEFAULT 0 COMMENT '总消耗卡路里',
    `total_duration` INT DEFAULT 0 COMMENT '总运动时长(分钟)',
    `total_distance` DECIMAL(10,2) DEFAULT 0 COMMENT '总距离(公里)',
    `rank` INT COMMENT '排名',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_type_period` (`user_id`, `type`, `period`),
    INDEX `idx_type_period_rank` (`type`, `period`, `rank`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排行榜统计表';

-- ============================================
-- 系统配置表
-- ============================================
CREATE TABLE `system_config` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `description` VARCHAR(500) COMMENT '描述',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ============================================
-- 初始化运动类型数据
-- ============================================
INSERT INTO `sport_type` (`name`, `category`, `calorie_rate`, `icon`, `description`, `sort`) VALUES
('跑步', 'aerobic', 10.0, '/static/icons/running.png', '有氧运动，提高心肺功能', 1),
('快走', 'aerobic', 5.0, '/static/icons/walking.png', '低强度有氧运动', 2),
('骑行', 'aerobic', 8.0, '/static/icons/cycling.png', '户外或室内单车', 3),
('游泳', 'aerobic', 12.0, '/static/icons/swimming.png', '全身性有氧运动', 4),
('跳绳', 'aerobic', 13.0, '/static/icons/rope.png', '高强度有氧运动', 5),
('健身操', 'aerobic', 7.0, '/static/icons/aerobics.png', '有氧健身操', 6),
('瑜伽', 'flexibility', 3.0, '/static/icons/yoga.png', '提高柔韧性和平衡', 7),
('普拉提', 'flexibility', 4.0, '/static/icons/pilates.png', '核心力量训练', 8),
('力量训练', 'strength', 6.0, '/static/icons/strength.png', '增肌塑形', 9),
('器械训练', 'strength', 7.0, '/static/icons/gym.png', '健身房器械训练', 10),
('哑铃训练', 'strength', 6.5, '/static/icons/dumbbell.png', '自由重量训练', 11),
('俯卧撑', 'strength', 5.0, '/static/icons/pushup.png', '上肢力量训练', 12),
('深蹲', 'strength', 5.5, '/static/icons/squat.png', '下肢力量训练', 13),
('平板支撑', 'strength', 4.0, '/static/icons/plank.png', '核心力量训练', 14),
('篮球', 'aerobic', 9.0, '/static/icons/basketball.png', '球类运动', 15),
('足球', 'aerobic', 9.5, '/static/icons/football.png', '球类运动', 16),
('羽毛球', 'aerobic', 7.5, '/static/icons/badminton.png', '球类运动', 17),
('乒乓球', 'aerobic', 6.0, '/static/icons/pingpong.png', '球类运动', 18),
('爬山', 'aerobic', 11.0, '/static/icons/hiking.png', '户外有氧运动', 19),
('拉伸', 'flexibility', 2.0, '/static/icons/stretch.png', '放松肌肉，提高柔韧性', 20);

-- ============================================
-- 初始化系统配置
-- ============================================
INSERT INTO `system_config` (`config_key`, `config_value`, `description`) VALUES
('wechat.appid', '', '微信小程序AppID'),
('wechat.secret', '', '微信小程序Secret'),
('ai.api.key', '', 'AI接口密钥'),
('ai.api.url', '', 'AI接口地址'),
('experience.per_minute', '10', '每分钟运动获得经验值'),
('level.up.base', '1000', '升级基础经验值');
