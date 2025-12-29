# API接口文档

## 基本信息

- **Base URL**: `http://your-domain/api`
- **认证方式**: JWT Token (放在Header的Authorization字段)
- **Header格式**: `Authorization: Bearer {token}`
- **额外Header**: `userId: {userId}` (部分接口需要)

## 通用响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1704038400000
}
```

### 状态码说明
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 无权限
- `404`: 资源不存在
- `500`: 服务器内部错误

---

## 1. 用户模块

### 1.1 微信登录

**接口**: `POST /user/login`

**请求参数**:
```json
{
  "code": "wx_login_code",      // 必填
  "nickname": "用户昵称",        // 选填
  "avatar": "头像URL",          // 选填
  "gender": 1                   // 选填, 0-未知 1-男 2-女
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "openid": "xxx",
    "nickname": "健身达人",
    "avatar": "https://xxx.png",
    "gender": 1,
    "level": 1,
    "experience": 0,
    "totalDays": 0,
    "continuousDays": 0,
    "token": "eyJhbGciOiJIUzUxMiJ9..."
  }
}
```

### 1.2 获取用户信息

**接口**: `GET /user/info/{userId}`

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "nickname": "健身达人",
    "avatar": "https://xxx.png",
    "gender": 1,
    "age": 25,
    "height": 175.0,
    "weight": 70.0,
    "targetWeight": 65.0,
    "level": 5,
    "experience": 3500,
    "totalDays": 100,
    "continuousDays": 15
  }
}
```

### 1.3 更新用户信息

**接口**: `PUT /user/update`

**Headers**: 需要Authorization

**请求参数**:
```json
{
  "id": 1,
  "age": 26,
  "height": 175.0,
  "weight": 68.0,
  "targetWeight": 65.0,
  "phone": "13800138000"
}
```

---

## 2. 运动记录模块

### 2.1 添加运动记录

**接口**: `POST /sport/record/add`

**Headers**: 
- `Authorization: Bearer {token}`
- `userId: {userId}`

**请求参数**:
```json
{
  "sportTypeId": 1,              // 必填, 运动类型ID
  "duration": 30,                // 必填, 时长(分钟)
  "distance": 5.0,               // 选填, 距离(公里)
  "calories": 300.0,             // 选填, 卡路里(自动计算)
  "steps": 5000,                 // 选填, 步数
  "heartRateAvg": 120,           // 选填, 平均心率
  "heartRateMax": 150,           // 选填, 最大心率
  "speedAvg": 10.0,              // 选填, 平均速度(km/h)
  "recordDate": "2024-01-15",    // 必填, 运动日期
  "startTime": "2024-01-15 08:00:00",
  "endTime": "2024-01-15 08:30:00",
  "note": "晨跑",
  "images": "[\"url1\", \"url2\"]",
  "syncSource": "manual"         // manual-手动, wechat-微信运动
}
```

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "userId": 1,
    "sportTypeId": 1,
    "duration": 30,
    "distance": 5.0,
    "calories": 300.0,
    "recordDate": "2024-01-15"
  }
}
```

### 2.2 分页查询运动记录

**接口**: `GET /sport/record/page`

**Headers**: `userId: {userId}`

**Query参数**:
- `current`: 当前页(默认1)
- `size`: 每页大小(默认10)

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": 1,
        "sportTypeId": 1,
        "duration": 30,
        "calories": 300.0,
        "recordDate": "2024-01-15"
      }
    ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

### 2.3 按日期范围查询

**接口**: `GET /sport/record/list`

**Headers**: `userId: {userId}`

**Query参数**:
- `startDate`: 开始日期(yyyy-MM-dd)
- `endDate`: 结束日期(yyyy-MM-dd)

### 2.4 统计数据

**接口**: `GET /sport/record/statistics`

**Headers**: `userId: {userId}`

**Query参数**:
- `startDate`: 开始日期
- `endDate`: 结束日期

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "recordCount": 15,
    "totalDuration": 450,
    "totalCalories": 4500.0,
    "totalDistance": 75.0,
    "avgCaloriesPerDay": 300.0,
    "avgDurationPerDay": 30.0
  }
}
```

### 2.5 每日统计数据

**接口**: `GET /sport/record/daily-statistics`

**Headers**: `userId: {userId}`

**Query参数**:
- `startDate`: 开始日期
- `endDate`: 结束日期

**响应示例**:
```json
{
  "code": 200,
  "data": [
    {
      "date": "2024-01-15",
      "duration": 30,
      "calories": 300.0,
      "distance": 5.0
    },
    {
      "date": "2024-01-16",
      "duration": 45,
      "calories": 450.0,
      "distance": 7.5
    }
  ]
}
```

### 2.6 删除记录

**接口**: `DELETE /sport/record/delete/{recordId}`

**Headers**: 
- `Authorization: Bearer {token}`
- `userId: {userId}`

---

## 3. 健身计划模块

### 3.1 创建健身计划

**接口**: `POST /plan/create`

**Headers**: 
- `Authorization: Bearer {token}`
- `userId: {userId}`

**请求参数**:
```json
{
  "name": "减脂训练计划",
  "goal": "lose_weight",          // lose_weight-减重, gain_muscle-增肌, keep_fit-保持
  "level": "intermediate",         // beginner-初级, intermediate-中级, advanced-高级
  "durationWeeks": 12,
  "frequencyPerWeek": 5,
  "planContent": "{...}",          // JSON格式的计划详情
  "aiGenerated": 1,                // 0-手动, 1-AI生成
  "startDate": "2024-01-15"
}
```

### 3.2 获取用户计划列表

**接口**: `GET /plan/list`

**Headers**: `userId: {userId}`

**Query参数**:
- `status`: 计划状态(0-已结束, 1-进行中, 2-已暂停)

### 3.3 获取计划详情

**接口**: `GET /plan/detail/{planId}`

### 3.4 AI生成计划

**接口**: `POST /plan/generate-ai`

**Headers**: `userId: {userId}`

**请求参数**:
```json
{
  "age": 25,
  "gender": 1,
  "weight": 70.0,
  "height": 175.0,
  "goal": "lose_weight",
  "level": "intermediate",
  "durationWeeks": 12,
  "frequencyPerWeek": 5,
  "preferences": "喜欢跑步和力量训练"
}
```

---

## 4. 社区模块

### 4.1 发布打卡动态

**接口**: `POST /community/post`

**Headers**: 
- `Authorization: Bearer {token}`
- `userId: {userId}`

**请求参数**:
```json
{
  "sportRecordId": 1,              // 关联的运动记录ID
  "content": "今天跑步5公里，感觉很棒！",
  "images": "[\"url1\", \"url2\"]",
  "location": "北京市朝阳区",
  "visibility": 1                   // 0-私密, 1-公开
}
```

### 4.2 获取动态列表

**接口**: `GET /community/posts`

**Query参数**:
- `current`: 当前页
- `size`: 每页大小
- `userId`: 查看指定用户的动态(选填)

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "userNickname": "健身达人",
        "userAvatar": "https://xxx.png",
        "content": "今天跑步5公里",
        "images": ["url1"],
        "location": "北京",
        "likeCount": 10,
        "commentCount": 5,
        "isLiked": false,
        "createdAt": "2024-01-15 08:00:00"
      }
    ],
    "total": 100
  }
}
```

### 4.3 点赞动态

**接口**: `POST /community/like/{postId}`

**Headers**: 
- `Authorization: Bearer {token}`
- `userId: {userId}`

### 4.4 取消点赞

**接口**: `DELETE /community/unlike/{postId}`

### 4.5 评论动态

**接口**: `POST /community/comment`

**请求参数**:
```json
{
  "postId": 1,
  "parentId": null,                // 父评论ID(回复评论时填写)
  "replyToUserId": null,           // 回复目标用户ID
  "content": "加油！"
}
```

### 4.6 获取评论列表

**接口**: `GET /community/comments/{postId}`

**Query参数**:
- `current`: 当前页
- `size`: 每页大小

---

## 5. 排行榜模块

### 5.1 获取排行榜

**接口**: `GET /leaderboard/rank`

**Query参数**:
- `type`: 排行类型(daily-日榜, weekly-周榜, monthly-月榜)
- `metric`: 指标(calories-卡路里, duration-时长, distance-距离)
- `limit`: 返回数量(默认100)

**响应示例**:
```json
{
  "code": 200,
  "data": [
    {
      "rank": 1,
      "userId": 1,
      "nickname": "健身达人",
      "avatar": "https://xxx.png",
      "totalCalories": 5000.0,
      "totalDuration": 500,
      "totalDistance": 100.0
    }
  ]
}
```

### 5.2 获取我的排名

**接口**: `GET /leaderboard/my-rank`

**Headers**: `userId: {userId}`

**Query参数**:
- `type`: 排行类型
- `metric`: 指标

---

## 6. 运动类型模块

### 6.1 获取运动类型列表

**接口**: `GET /sport/type/list`

**Query参数**:
- `category`: 分类(aerobic-有氧, strength-力量, flexibility-柔韧)

**响应示例**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "跑步",
      "category": "aerobic",
      "calorieRate": 10.0,
      "icon": "/static/icons/running.png",
      "description": "有氧运动，提高心肺功能"
    }
  ]
}
```

---

## 错误码说明

| 错误码 | 说明 |
|-------|------|
| 40001 | 参数错误 |
| 40101 | 未登录 |
| 40102 | Token无效 |
| 40103 | Token过期 |
| 40301 | 无权限 |
| 40401 | 资源不存在 |
| 50001 | 服务器内部错误 |
| 50002 | 数据库错误 |
| 50003 | 外部接口调用失败 |

---

## 调用示例

### JavaScript (uni-app)
```javascript
// 添加运动记录
uni.request({
  url: 'http://your-domain/api/sport/record/add',
  method: 'POST',
  header: {
    'Authorization': 'Bearer ' + token,
    'userId': userId,
    'Content-Type': 'application/json'
  },
  data: {
    sportTypeId: 1,
    duration: 30,
    recordDate: '2024-01-15'
  },
  success: (res) => {
    console.log(res.data);
  }
});
```

### Java (HTTP Client)
```java
HttpHeaders headers = new HttpHeaders();
headers.set("Authorization", "Bearer " + token);
headers.set("userId", userId);
headers.setContentType(MediaType.APPLICATION_JSON);

Map<String, Object> data = new HashMap<>();
data.put("sportTypeId", 1);
data.put("duration", 30);

HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
ResponseEntity<String> response = restTemplate.postForEntity(
    "http://your-domain/api/sport/record/add",
    request,
    String.class
);
```
