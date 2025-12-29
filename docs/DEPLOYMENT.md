# 部署文档

## 📋 目录
1. [环境准备](#环境准备)
2. [数据库部署](#数据库部署)
3. [后端部署](#后端部署)
4. [前端部署](#前端部署)
5. [生产环境配置](#生产环境配置)
6. [常见问题](#常见问题)

---

## 环境准备

### 服务器要求
- **操作系统**: Linux (CentOS 7+, Ubuntu 18.04+)
- **CPU**: 2核及以上
- **内存**: 4GB及以上
- **磁盘**: 50GB及以上

### 软件要求
- **Java**: JDK 1.8+
- **MySQL**: 8.0+
- **Redis**: 5.0+
- **Nginx**: 1.18+ (可选)
- **Maven**: 3.6+

### 安装Java
```bash
# CentOS
sudo yum install java-1.8.0-openjdk java-1.8.0-openjdk-devel

# Ubuntu
sudo apt update
sudo apt install openjdk-8-jdk

# 验证安装
java -version
```

### 安装MySQL
```bash
# CentOS 7
wget https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
sudo rpm -ivh mysql80-community-release-el7-3.noarch.rpm
sudo yum install mysql-server

# Ubuntu
sudo apt update
sudo apt install mysql-server

# 启动MySQL
sudo systemctl start mysqld
sudo systemctl enable mysqld

# 获取临时密码（CentOS）
sudo grep 'temporary password' /var/log/mysqld.log

# 修改root密码
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'YourNewPassword123!';
FLUSH PRIVILEGES;
```

### 安装Redis
```bash
# CentOS
sudo yum install redis

# Ubuntu
sudo apt install redis-server

# 启动Redis
sudo systemctl start redis
sudo systemctl enable redis

# 验证
redis-cli ping
# 应返回 PONG
```

### 安装Maven
```bash
cd /opt
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
tar -xzf apache-maven-3.9.6-bin.tar.gz
sudo mv apache-maven-3.9.6 /usr/local/maven

# 配置环境变量
echo 'export MAVEN_HOME=/usr/local/maven' >> ~/.bashrc
echo 'export PATH=$MAVEN_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc

# 验证
mvn -v
```

---

## 数据库部署

### 1. 创建数据库
```bash
mysql -u root -p
```

```sql
-- 创建数据库
CREATE DATABASE fitness_tracker DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建专用用户（推荐）
CREATE USER 'fitness_user'@'%' IDENTIFIED BY 'FitnessPass123!';
GRANT ALL PRIVILEGES ON fitness_tracker.* TO 'fitness_user'@'%';
FLUSH PRIVILEGES;

-- 退出
EXIT;
```

### 2. 导入数据库表结构
```bash
mysql -u fitness_user -p fitness_tracker < database/schema.sql
```

### 3. 验证数据库
```bash
mysql -u fitness_user -p fitness_tracker
```

```sql
-- 查看所有表
SHOW TABLES;

-- 应该看到以下表：
-- user, sport_type, sport_record, weight_record, 
-- fitness_plan, plan_execution, check_in_post, 
-- post_like, post_comment, leaderboard, system_config

-- 查看运动类型数据
SELECT * FROM sport_type;
```

---

## 后端部署

### 方式一：JAR包部署（推荐）

#### 1. 修改配置文件
```bash
cd backend/src/main/resources
vi application.yml
```

修改以下配置：
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fitness_tracker?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: fitness_user
    password: FitnessPass123!
  
  redis:
    host: localhost
    port: 6379
    password: # 如果设置了密码

wechat:
  miniapp:
    appid: your_real_appid
    secret: your_real_secret

ai:
  api:
    url: https://api.openai.com/v1/chat/completions
    key: your_api_key

file:
  upload:
    path: /data/fitness-tracker/uploads
```

#### 2. 编译打包
```bash
cd backend
mvn clean package -DskipTests

# 打包后的文件位于 target/fitness-tracker-ai-1.0.0.jar
```

#### 3. 创建运行目录
```bash
sudo mkdir -p /opt/fitness-tracker
sudo cp target/fitness-tracker-ai-1.0.0.jar /opt/fitness-tracker/
sudo mkdir -p /data/fitness-tracker/uploads
sudo chmod -R 755 /data/fitness-tracker
```

#### 4. 创建systemd服务
```bash
sudo vi /etc/systemd/system/fitness-tracker.service
```

添加以下内容：
```ini
[Unit]
Description=Fitness Tracker AI Backend
After=syslog.target network.target

[Service]
Type=simple
User=root
WorkingDirectory=/opt/fitness-tracker
ExecStart=/usr/bin/java -jar /opt/fitness-tracker/fitness-tracker-ai-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

#### 5. 启动服务
```bash
# 重载systemd配置
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start fitness-tracker

# 设置开机自启
sudo systemctl enable fitness-tracker

# 查看状态
sudo systemctl status fitness-tracker

# 查看日志
sudo journalctl -u fitness-tracker -f
```

### 方式二：Docker部署

#### 1. 创建Dockerfile
```bash
cd backend
vi Dockerfile
```

```dockerfile
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/fitness-tracker-ai-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
```

#### 2. 构建镜像
```bash
mvn clean package -DskipTests
docker build -t fitness-tracker-backend:1.0.0 .
```

#### 3. 运行容器
```bash
docker run -d \
  --name fitness-tracker \
  -p 8080:8080 \
  -v /data/fitness-tracker/uploads:/data/fitness-tracker/uploads \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/fitness_tracker \
  -e SPRING_DATASOURCE_USERNAME=fitness_user \
  -e SPRING_DATASOURCE_PASSWORD=FitnessPass123! \
  -e SPRING_REDIS_HOST=host.docker.internal \
  fitness-tracker-backend:1.0.0
```

---

## 前端部署

### 微信小程序发布

#### 1. 配置小程序信息
```json
// miniprogram/manifest.json
{
  "mp-weixin": {
    "appid": "your_real_appid"
  }
}
```

#### 2. 修改API地址
```javascript
// miniprogram/utils/request.js
const BASE_URL = 'https://api.your-domain.com/api';
```

#### 3. 使用HBuilderX打包
1. 打开HBuilderX
2. 选择项目
3. 发行 -> 小程序-微信
4. 填写版本号和项目备注
5. 点击发行

#### 4. 上传到微信平台
1. 打开微信开发者工具
2. 导入项目
3. 点击"上传"按钮
4. 填写版本号和项目备注
5. 上传成功

#### 5. 提交审核
1. 登录[微信公众平台](https://mp.weixin.qq.com/)
2. 进入小程序管理后台
3. 版本管理 -> 开发版本 -> 提交审核
4. 填写审核信息
5. 等待审核通过

---

## 生产环境配置

### Nginx反向代理配置

#### 1. 安装Nginx
```bash
# CentOS
sudo yum install nginx

# Ubuntu
sudo apt install nginx
```

#### 2. 配置SSL证书（推荐）
```bash
# 使用Let's Encrypt免费证书
sudo yum install certbot python3-certbot-nginx
sudo certbot --nginx -d api.your-domain.com
```

#### 3. 配置Nginx
```bash
sudo vi /etc/nginx/conf.d/fitness-tracker.conf
```

```nginx
# HTTP重定向到HTTPS
server {
    listen 80;
    server_name api.your-domain.com;
    return 301 https://$server_name$request_uri;
}

# HTTPS配置
server {
    listen 443 ssl http2;
    server_name api.your-domain.com;

    ssl_certificate /etc/letsencrypt/live/api.your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.your-domain.com/privkey.pem;

    # SSL优化
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # 日志
    access_log /var/log/nginx/fitness-tracker-access.log;
    error_log /var/log/nginx/fitness-tracker-error.log;

    # 反向代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 超时设置
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 文件上传大小限制
    client_max_body_size 10M;
}
```

#### 4. 重启Nginx
```bash
# 测试配置
sudo nginx -t

# 重启Nginx
sudo systemctl restart nginx
sudo systemctl enable nginx
```

### 防火墙配置
```bash
# CentOS/RHEL
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload

# Ubuntu (UFW)
sudo ufw allow 'Nginx Full'
sudo ufw enable
```

### MySQL优化配置
```bash
sudo vi /etc/my.cnf
```

```ini
[mysqld]
max_connections = 500
innodb_buffer_pool_size = 2G
innodb_log_file_size = 256M
query_cache_size = 128M
tmp_table_size = 64M
max_heap_table_size = 64M
```

重启MySQL：
```bash
sudo systemctl restart mysqld
```

### Redis优化配置
```bash
sudo vi /etc/redis/redis.conf
```

```conf
maxmemory 1gb
maxmemory-policy allkeys-lru
appendonly yes
```

重启Redis：
```bash
sudo systemctl restart redis
```

---

## 监控与日志

### 1. 应用日志查看
```bash
# systemd服务日志
sudo journalctl -u fitness-tracker -f

# 应用日志文件（如果配置了）
tail -f /var/log/fitness-tracker/app.log
```

### 2. Nginx日志
```bash
# 访问日志
tail -f /var/log/nginx/fitness-tracker-access.log

# 错误日志
tail -f /var/log/nginx/fitness-tracker-error.log
```

### 3. 系统监控
```bash
# 查看系统资源
top
htop

# 查看Java进程
jps -v

# 查看端口占用
netstat -tulpn | grep 8080
```

---

## 备份策略

### 数据库备份
```bash
# 创建备份脚本
sudo vi /opt/backup/backup-mysql.sh
```

```bash
#!/bin/bash
BACKUP_DIR="/data/backup/mysql"
DATE=$(date +%Y%m%d_%H%M%S)
mkdir -p $BACKUP_DIR

mysqldump -u fitness_user -pFitnessPass123! fitness_tracker > $BACKUP_DIR/fitness_tracker_$DATE.sql

# 保留最近7天的备份
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
```

```bash
# 添加执行权限
sudo chmod +x /opt/backup/backup-mysql.sh

# 添加到crontab，每天凌晨2点备份
crontab -e
0 2 * * * /opt/backup/backup-mysql.sh
```

### Redis备份
```bash
# Redis会自动生成dump.rdb文件
# 定期复制到备份目录
cp /var/lib/redis/dump.rdb /data/backup/redis/dump_$(date +%Y%m%d).rdb
```

---

## 常见问题

### 1. 后端启动失败
```bash
# 检查端口占用
netstat -tulpn | grep 8080

# 检查Java进程
jps -v

# 查看详细日志
sudo journalctl -u fitness-tracker -n 100
```

### 2. 数据库连接失败
```bash
# 检查MySQL服务
sudo systemctl status mysqld

# 测试连接
mysql -u fitness_user -p -h localhost fitness_tracker

# 检查防火墙
sudo firewall-cmd --list-all
```

### 3. Redis连接失败
```bash
# 检查Redis服务
sudo systemctl status redis

# 测试连接
redis-cli ping

# 检查配置
cat /etc/redis/redis.conf | grep bind
```

### 4. 小程序无法请求后端
- 检查request合法域名配置
- 确认https证书有效
- 检查服务器防火墙设置
- 查看Nginx日志

### 5. 文件上传失败
```bash
# 检查目录权限
ls -la /data/fitness-tracker/uploads
sudo chmod -R 755 /data/fitness-tracker/uploads

# 检查磁盘空间
df -h
```

---

## 性能优化建议

1. **数据库优化**
   - 添加适当索引
   - 定期清理过期数据
   - 开启查询缓存

2. **Redis优化**
   - 设置合理的内存限制
   - 使用持久化策略
   - 热点数据缓存

3. **应用优化**
   - 使用连接池
   - 开启GZIP压缩
   - 图片CDN加速

4. **服务器优化**
   - 增加内存和CPU
   - 使用SSD硬盘
   - 配置负载均衡

---

## 联系支持

如果遇到部署问题，请联系：
- Email: support@fitness-tracker.com
- 技术文档: https://docs.fitness-tracker.com
