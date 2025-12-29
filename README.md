# 🏃‍♂️ 基于AI的健身数据追踪小程序 (Fitness Tracker AI)

## 📋 项目简介

这是一款集成AI智能的健身数据追踪微信小程序，帮助用户记录运动数据、生成个性化训练计划、分析健身效果，并通过社区功能分享健身成果。

## ✨ 核心功能

### 1. 用户管理
- ✅ 微信一键登录
- ✅ 个人信息管理（身高、体重、目标等）
- ✅ 等级系统（运动获得经验值升级）
- ✅ 打卡天数统计（累计/连续）

### 2. 运动数据录入
- ✅ 支持20+种运动类型（跑步、力量训练、瑜伽等）
- ✅ 多维度数据记录（时长、距离、卡路里、心率等）
- ✅ 手动录入 + 微信运动API自动同步
- ✅ 图片上传记录运动瞬间
- ✅ 自动计算卡路里消耗

### 3. AI健身计划
- ✅ 个性化训练计划生成（基于用户信息和目标）
- ✅ 三种目标：减重、增肌、保持
- ✅ 三种难度：初级、中级、高级
- ✅ 计划执行跟踪和完成度统计
- ✅ AI模型集成（支持GPT/百度/Keep等平台）

### 4. 数据可视化与分析
- ✅ ECharts图表展示运动趋势
- ✅ 多维度统计（日/周/月/年）
- ✅ 卡路里消耗曲线
- ✅ 体重变化趋势
- ✅ 运动目标完成度分析
- ✅ 运动类型分布统计

### 5. 社区互动
- ✅ 运动打卡动态发布
- ✅ 图片分享
- ✅ 点赞、评论、回复
- ✅ 动态流浏览
- ✅ 排行榜（日榜/周榜/月榜）
  - 卡路里消耗榜
  - 运动时长榜
  - 运动距离榜

## 🏗️ 技术架构

### 后端技术栈
```
├── Spring Boot 2.7.18      # 核心框架
├── MyBatis-Plus 3.5.3      # ORM框架
├── MySQL 8.0+              # 主数据库
├── Redis                   # 缓存/排行榜
├── JWT                     # 身份认证
├── Hutool                  # 工具库
├── FastJSON2               # JSON处理
└── Lombok                  # 简化开发
```

### 前端技术栈
```
├── uni-app                 # 跨平台框架
├── Vue.js                  # MVVM框架
├── uView UI                # UI组件库
├── uCharts                 # 图表库
└── Vuex                    # 状态管理
```

### 数据库设计
- **user**: 用户表
- **sport_type**: 运动类型表
- **sport_record**: 运动记录表
- **weight_record**: 体重记录表
- **fitness_plan**: 健身计划表
- **plan_execution**: 计划执行记录表
- **check_in_post**: 打卡动态表
- **post_like**: 点赞表
- **post_comment**: 评论表
- **leaderboard**: 排行榜统计表
- **system_config**: 系统配置表

## 📦 项目结构

```
fitness-tracker-ai/
├── backend/                          # 后端项目
│   ├── src/main/java/com/fitness/tracker/
│   │   ├── config/                   # 配置类
│   │   │   ├── MybatisPlusConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   └── CorsConfig.java
│   │   ├── controller/               # 控制器
│   │   │   ├── UserController.java
│   │   │   ├── SportRecordController.java
│   │   │   ├── FitnessPlanController.java
│   │   │   └── CommunityController.java
│   │   ├── service/                  # 服务接口
│   │   │   └── impl/                 # 服务实现
│   │   ├── mapper/                   # 数据访问层
│   │   ├── entity/                   # 实体类
│   │   ├── dto/                      # 数据传输对象
│   │   ├── vo/                       # 视图对象
│   │   ├── utils/                    # 工具类
│   │   │   ├── JwtUtil.java
│   │   │   └── RedisUtil.java
│   │   ├── common/                   # 通用类
│   │   │   └── Result.java
│   │   └── FitnessTrackerApplication.java
│   ├── src/main/resources/
│   │   ├── mapper/                   # MyBatis映射文件
│   │   └── application.yml           # 配置文件
│   └── pom.xml                       # Maven配置
│
├── miniprogram/                      # 小程序前端
│   ├── pages/                        # 页面
│   │   ├── index/                    # 首页
│   │   ├── sport/                    # 运动记录
│   │   ├── plan/                     # 健身计划
│   │   ├── community/                # 社区
│   │   └── profile/                  # 个人中心
│   ├── components/                   # 组件
│   ├── static/                       # 静态资源
│   │   ├── images/
│   │   └── icons/
│   ├── utils/                        # 工具类
│   │   └── request.js                # 网络请求封装
│   ├── api/                          # API接口
│   │   ├── user.js
│   │   ├── sport.js
│   │   ├── plan.js
│   │   └── community.js
│   ├── store/                        # Vuex状态管理
│   ├── pages.json                    # 页面配置
│   └── manifest.json                 # 应用配置
│
├── database/                         # 数据库脚本
│   └── schema.sql                    # 数据库表结构
│
├── docs/                             # 文档
│   ├── API.md                        # API接口文档
│   ├── DATABASE.md                   # 数据库设计文档
│   └── DEPLOYMENT.md                 # 部署文档
│
└── README.md                         # 项目说明
```

## 🚀 快速开始

### 环境要求
- Java 8+
- MySQL 8.0+
- Redis 5.0+
- Maven 3.6+
- Node.js 14+
- 微信开发者工具

### 后端启动

1. **创建数据库**
```bash
# 导入数据库脚本
mysql -u root -p < database/schema.sql
```

2. **配置application.yml**
```yaml
# 修改数据库连接信息
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fitness_tracker
    username: your_username
    password: your_password
  
  # 配置Redis
  redis:
    host: localhost
    port: 6379

# 配置微信小程序
wechat:
  miniapp:
    appid: your_appid
    secret: your_secret

# 配置AI接口
ai:
  api:
    url: your_ai_api_url
    key: your_api_key
```

3. **启动后端服务**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080/api` 启动

### 前端启动

1. **配置小程序AppID**
```json
// miniprogram/manifest.json
{
  "mp-weixin": {
    "appid": "your_appid"
  }
}
```

2. **修改API地址**
```javascript
// miniprogram/utils/request.js
const BASE_URL = 'http://your-backend-url/api';
```

3. **使用HBuilderX导入项目**
- 打开HBuilderX
- 文件 -> 导入 -> 从本地目录导入
- 选择miniprogram目录

4. **运行到微信开发者工具**
- 运行 -> 运行到小程序模拟器 -> 微信开发者工具

## 📊 核心业务流程

### 1. 用户登录流程
```
用户打开小程序 
-> wx.login获取code 
-> 调用后端/user/login接口 
-> 后端调用微信API获取openid 
-> 生成JWT Token 
-> 返回用户信息和Token
-> 前端存储Token用于后续请求
```

### 2. 运动记录流程
```
用户填写运动信息 
-> 选择运动类型 
-> 输入时长/距离等数据 
-> 后端自动计算卡路里 
-> 保存记录 
-> 增加用户经验值 
-> 更新今日统计数据
```

### 3. AI计划生成流程
```
用户输入基本信息（年龄、性别、体重、目标） 
-> 选择训练目标和难度 
-> 调用AI API生成个性化计划 
-> 保存计划到数据库 
-> 用户可查看和执行计划
```

### 4. 社区互动流程
```
用户发布打卡动态 
-> 上传图片和文字 
-> 保存到动态表 
-> 其他用户浏览动态流 
-> 点赞/评论互动 
-> 更新动态统计数据
```

## 🔐 API接口示例

### 用户登录
```http
POST /api/user/login
Content-Type: application/json

{
  "code": "wx_login_code",
  "nickname": "用户昵称",
  "avatar": "头像URL",
  "gender": 1
}

Response:
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "nickname": "用户昵称",
    "avatar": "头像URL",
    "level": 1,
    "experience": 0,
    "token": "jwt_token"
  }
}
```

### 添加运动记录
```http
POST /api/sport/record/add
Authorization: Bearer {token}
userId: {userId}
Content-Type: application/json

{
  "sportTypeId": 1,
  "duration": 30,
  "distance": 5.0,
  "recordDate": "2024-01-15",
  "note": "晨跑"
}

Response:
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "userId": 1,
    "sportTypeId": 1,
    "duration": 30,
    "calories": 300.0,
    "distance": 5.0
  }
}
```

## 📈 数据统计功能

### 统计维度
- **时间维度**: 日/周/月/年
- **数据维度**: 
  - 运动次数
  - 总时长
  - 总卡路里
  - 总距离
  - 平均心率

### 可视化图表
- 折线图: 运动趋势
- 柱状图: 每日数据对比
- 饼图: 运动类型分布
- 环形图: 目标完成度

## 🏆 排行榜机制

### 排行类型
1. **日榜**: 当日数据排名
2. **周榜**: 本周累计排名
3. **月榜**: 本月累计排名

### 排名指标
- 卡路里消耗总量
- 运动时长总量
- 运动距离总量

### 实现方式
使用Redis的ZSet（有序集合）实现高性能排行榜查询

## 🤖 AI集成说明

### 支持的AI平台
1. **OpenAI GPT**: 通用AI对话
2. **百度健康AI**: 专业健身建议
3. **Keep开放平台**: 运动数据分析
4. **自定义AI**: 支持任意兼容接口

### AI功能应用
- 个性化训练计划生成
- 运动数据分析和建议
- 饮食建议
- 健康风险评估

## 🔧 性能优化

### 后端优化
- Redis缓存热点数据
- MyBatis-Plus分页查询
- 数据库索引优化
- 接口响应压缩

### 前端优化
- 图片懒加载
- 列表虚拟滚动
- 数据分页加载
- 本地缓存策略

## 📱 小程序截图

（此处可添加实际截图）

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

### 开发规范
- 遵循阿里巴巴Java开发规范
- 使用Vue风格指南
- 提交前进行代码格式化
- 编写必要的注释和文档

## 📄 开源协议

MIT License

## 👥 联系方式

- 项目作者: Fitness Tracker Team
- Email: contact@fitness-tracker.com
- 技术支持: support@fitness-tracker.com

## 🔄 版本历史

### v1.0.0 (2024-01-15)
- ✅ 完成核心功能开发
- ✅ 用户登录和个人信息管理
- ✅ 运动数据录入和统计
- ✅ AI健身计划生成
- ✅ 数据可视化
- ✅ 社区互动功能
- ✅ 排行榜系统

## 🎯 未来规划

- [ ] 运动视频教学
- [ ] 语音指导功能
- [ ] 健康报告生成
- [ ] 好友系统
- [ ] 运动挑战赛
- [ ] 智能穿戴设备对接
- [ ] 营养膳食管理
- [ ] 专业教练在线指导

---

**如果这个项目对你有帮助，请给个 ⭐ Star 支持一下！**
