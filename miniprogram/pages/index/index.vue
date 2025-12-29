<template>
    <view class="container">
        <!-- 用户信息卡片 -->
        <view class="user-card">
            <view class="user-info">
                <image class="avatar" :src="userInfo.avatar || '/static/images/default-avatar.png'" />
                <view class="info">
                    <text class="nickname">{{ userInfo.nickname || '健身达人' }}</text>
                    <view class="level">
                        <text class="level-text">Lv.{{ userInfo.level || 1 }}</text>
                        <progress :percent="experiencePercent" stroke-width="4" activeColor="#3cc51f" />
                    </view>
                </view>
            </view>
            <view class="stats">
                <view class="stat-item">
                    <text class="stat-value">{{ userInfo.totalDays || 0 }}</text>
                    <text class="stat-label">累计天数</text>
                </view>
                <view class="stat-item">
                    <text class="stat-value">{{ userInfo.continuousDays || 0 }}</text>
                    <text class="stat-label">连续打卡</text>
                </view>
            </view>
        </view>

        <!-- 今日数据 -->
        <view class="today-data">
            <view class="section-title">今日数据</view>
            <view class="data-grid">
                <view class="data-item">
                    <text class="data-value">{{ todayData.calories || 0 }}</text>
                    <text class="data-label">卡路里(kcal)</text>
                </view>
                <view class="data-item">
                    <text class="data-value">{{ todayData.duration || 0 }}</text>
                    <text class="data-label">时长(分钟)</text>
                </view>
                <view class="data-item">
                    <text class="data-value">{{ todayData.distance || 0 }}</text>
                    <text class="data-label">距离(km)</text>
                </view>
                <view class="data-item">
                    <text class="data-value">{{ todayData.steps || 0 }}</text>
                    <text class="data-label">步数</text>
                </view>
            </view>
        </view>

        <!-- 快捷操作 -->
        <view class="quick-actions">
            <view class="section-title">快捷操作</view>
            <view class="action-grid">
                <view class="action-item" @click="goToAddRecord">
                    <image src="/static/icons/add-record.png" class="action-icon" />
                    <text class="action-text">添加记录</text>
                </view>
                <view class="action-item" @click="goToCreatePlan">
                    <image src="/static/icons/create-plan.png" class="action-icon" />
                    <text class="action-text">AI计划</text>
                </view>
                <view class="action-item" @click="goToStatistics">
                    <image src="/static/icons/statistics.png" class="action-icon" />
                    <text class="action-text">数据统计</text>
                </view>
                <view class="action-item" @click="syncWeChatSport">
                    <image src="/static/icons/sync.png" class="action-icon" />
                    <text class="action-text">同步数据</text>
                </view>
            </view>
        </view>

        <!-- 本周趋势 -->
        <view class="weekly-trend">
            <view class="section-title">本周趋势</view>
            <view class="chart-container">
                <!-- 这里可以集成图表组件 -->
                <text class="placeholder">图表区域</text>
            </view>
        </view>
    </view>
</template>

<script>
import { getUserInfo } from '@/api/user';
import { getSportStatistics } from '@/api/sport';

export default {
    data() {
        return {
            userInfo: {},
            todayData: {},
            experiencePercent: 0
        };
    },
    onLoad() {
        this.loadUserInfo();
        this.loadTodayData();
    },
    onPullDownRefresh() {
        this.loadUserInfo();
        this.loadTodayData();
        setTimeout(() => {
            uni.stopPullDownRefresh();
        }, 1000);
    },
    methods: {
        async loadUserInfo() {
            try {
                const userId = uni.getStorageSync('userId');
                if (userId) {
                    const res = await getUserInfo(userId);
                    this.userInfo = res.data;
                    // 计算经验值百分比
                    const levelUpExp = 1000 * this.userInfo.level;
                    this.experiencePercent = (this.userInfo.experience / levelUpExp) * 100;
                }
            } catch (error) {
                console.error('加载用户信息失败', error);
            }
        },
        async loadTodayData() {
            try {
                const today = new Date();
                const startDate = this.formatDate(today);
                const endDate = this.formatDate(today);
                
                const res = await getSportStatistics({ startDate, endDate });
                if (res.data) {
                    this.todayData = {
                        calories: res.data.totalCalories || 0,
                        duration: res.data.totalDuration || 0,
                        distance: res.data.totalDistance || 0,
                        steps: 0 // 步数需要从运动记录中累加
                    };
                }
            } catch (error) {
                console.error('加载今日数据失败', error);
            }
        },
        formatDate(date) {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        },
        goToAddRecord() {
            uni.navigateTo({ url: '/pages/sport/add-record' });
        },
        goToCreatePlan() {
            uni.navigateTo({ url: '/pages/plan/create-plan' });
        },
        goToStatistics() {
            uni.navigateTo({ url: '/pages/profile/statistics' });
        },
        syncWeChatSport() {
            uni.showToast({
                title: '同步功能开发中',
                icon: 'none'
            });
        }
    }
};
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    background: #f5f5f5;
    padding: 20rpx;
}

.user-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    color: #fff;
}

.user-info {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
}

.avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: 60rpx;
    margin-right: 20rpx;
}

.info {
    flex: 1;
}

.nickname {
    font-size: 36rpx;
    font-weight: bold;
    display: block;
    margin-bottom: 10rpx;
}

.level {
    display: flex;
    align-items: center;
}

.level-text {
    font-size: 24rpx;
    margin-right: 10rpx;
}

.stats {
    display: flex;
    justify-content: space-around;
}

.stat-item {
    text-align: center;
}

.stat-value {
    display: block;
    font-size: 48rpx;
    font-weight: bold;
}

.stat-label {
    font-size: 24rpx;
    opacity: 0.8;
}

.section-title {
    font-size: 32rpx;
    font-weight: bold;
    margin: 30rpx 0 20rpx;
}

.today-data {
    background: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
}

.data-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20rpx;
}

.data-item {
    text-align: center;
    padding: 20rpx;
    background: #f5f5f5;
    border-radius: 10rpx;
}

.data-value {
    display: block;
    font-size: 40rpx;
    font-weight: bold;
    color: #3cc51f;
}

.data-label {
    font-size: 24rpx;
    color: #666;
}

.quick-actions {
    background: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
}

.action-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20rpx;
}

.action-item {
    text-align: center;
}

.action-icon {
    width: 80rpx;
    height: 80rpx;
    margin-bottom: 10rpx;
}

.action-text {
    display: block;
    font-size: 24rpx;
    color: #333;
}

.weekly-trend {
    background: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
}

.chart-container {
    height: 400rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f5f5f5;
    border-radius: 10rpx;
}

.placeholder {
    color: #999;
}
</style>
