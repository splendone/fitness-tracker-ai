# ⚡ 快速启动指南

## 🎯 5分钟快速体验

### 前置条件
- ✅ Java 8+
- ✅ MySQL 8.0+
- ✅ Redis 5.0+
- ✅ Maven 3.6+

---

## 📝 步骤一：克隆项目

```bash
git clone https://github.com/splendone/fitness-tracker-ai.git
cd fitness-tracker-ai
```

---

## 🗄️ 步骤二：配置数据库

### 1. 创建数据库
```bash
mysql -u root -p
```

```sql
CREATE DATABASE fitness_tracker DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

### 2. 导入表结构
```bash
mysql -u root -p fitness_tracker < database/schema.sql
```

---

## ⚙️ 步骤三：配置后端

### 1. 修改配置文件
编辑 `backend/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fitness_tracker
    username: root          # 修改为你的MySQL用户名
    password: root          # 修改为你的MySQL密码
  
  redis:
    host: localhost
    port: 6379
    password:              # 如果有密码就填写

wechat:
  miniapp:
    appid: your_appid      # 暂时可以不改
    secret: your_secret    # 暂时可以不改
```

### 2. 启动后端服务
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

看到以下信息表示启动成功：
```
========================================
  Fitness Tracker AI Backend Started  
  API: http://localhost:8080/api      
========================================
```

---

## 🧪 步骤四：测试API

### 使用curl测试
```bash
# 测试获取运动类型列表
curl http://localhost:8080/api/sport/type/list

# 应该返回JSON格式的运动类型数据
```

### 使用Postman测试
1. 导入以下请求：
   - GET `http://localhost:8080/api/sport/type/list`
2. 发送请求，应该看到20种运动类型数据

---

## 📱 步骤五：配置小程序（可选）

### 使用HBuilderX
1. 下载并安装 [HBuilderX](https://www.dcloud.io/hbuilderx.html)
2. 打开HBuilderX
3. 文件 -> 导入 -> 从本地目录导入
4. 选择 `miniprogram` 目录
5. 运行 -> 运行到浏览器（可以先在浏览器预览）

### 修改API地址
编辑 `miniprogram/utils/request.js`
```javascript
const BASE_URL = 'http://localhost:8080/api';
```

---

## 🔍 验证安装

### 1. 检查数据库
```bash
mysql -u root -p fitness_tracker
```
```sql
-- 查看所有表
SHOW TABLES;

-- 查看运动类型数据
SELECT * FROM sport_type;
-- 应该有20条数据

-- 退出
EXIT;
```

### 2. 检查后端服务
访问：http://localhost:8080/api/sport/type/list

应该看到类似这样的JSON响应：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "跑步",
      "category": "aerobic",
      "calorieRate": 10.0
    },
    ...
  ]
}
```

---

## 🎮 开始使用

### 核心功能测试流程

#### 1. 用户登录（需要微信小程序）
```http
POST http://localhost:8080/api/user/login
Content-Type: application/json

{
  "code": "test_code",
  "nickname": "测试用户",
  "avatar": "https://example.com/avatar.png",
  "gender": 1
}
```

#### 2. 添加运动记录
```http
POST http://localhost:8080/api/sport/record/add
Authorization: Bearer {token}
userId: {userId}
Content-Type: application/json

{
  "sportTypeId": 1,
  "duration": 30,
  "distance": 5.0,
  "recordDate": "2024-01-15"
}
```

#### 3. 查询统计数据
```http
GET http://localhost:8080/api/sport/record/statistics?startDate=2024-01-01&endDate=2024-01-31
userId: {userId}
```

---

## 🐛 常见问题

### Q1: 后端启动失败
**A**: 检查MySQL和Redis是否启动
```bash
# 检查MySQL
sudo systemctl status mysqld

# 检查Redis
sudo systemctl status redis
```

### Q2: 数据库连接失败
**A**: 检查配置文件中的数据库密码是否正确

### Q3: 端口被占用
**A**: 修改application.yml中的端口
```yaml
server:
  port: 8081  # 改成其他端口
```

### Q4: Maven下载依赖慢
**A**: 配置阿里云镜像
编辑 `~/.m2/settings.xml`
```xml
<mirror>
  <id>aliyun</id>
  <mirrorOf>central</mirrorOf>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

---

## 📚 下一步

### 学习路径
1. ✅ 快速启动（你已完成）
2. 📖 阅读 [README.md](README.md) 了解完整功能
3. 📖 阅读 [API.md](docs/API.md) 学习接口使用
4. 📖 阅读 [DEPLOYMENT.md](docs/DEPLOYMENT.md) 了解生产部署
5. 💻 开始二次开发或定制

### 开发建议
- 使用IDEA或Eclipse开发后端
- 使用HBuilderX开发小程序
- 使用Postman测试API
- 使用MySQL Workbench管理数据库

---

## 🆘 获取帮助

- **GitHub Issues**: https://github.com/splendone/fitness-tracker-ai/issues
- **项目文档**: 查看docs目录
- **API文档**: docs/API.md
- **部署文档**: docs/DEPLOYMENT.md

---

## ⭐ 项目地址

**GitHub**: https://github.com/splendone/fitness-tracker-ai

如果觉得有帮助，请给个Star ⭐！

---

**祝你使用愉快！🎉**
