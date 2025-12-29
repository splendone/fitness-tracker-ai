/**
 * 用户相关API
 */
import { get, post, put } from '@/utils/request';

/**
 * 微信登录
 */
export function wxLogin(data) {
    return post('/user/login', data);
}

/**
 * 获取用户信息
 */
export function getUserInfo(userId) {
    return get(`/user/info/${userId}`);
}

/**
 * 更新用户信息
 */
export function updateUserInfo(data) {
    return put('/user/update', data);
}
