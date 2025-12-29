/**
 * 网络请求封装
 */

const BASE_URL = 'http://localhost:8080/api';

/**
 * 请求拦截器
 */
function request(options) {
    return new Promise((resolve, reject) => {
        // 从缓存获取token
        const token = uni.getStorageSync('token');
        const userId = uni.getStorageSync('userId');

        uni.request({
            url: BASE_URL + options.url,
            method: options.method || 'GET',
            data: options.data || {},
            header: {
                'Content-Type': 'application/json',
                'Authorization': token ? `Bearer ${token}` : '',
                'userId': userId || '',
                ...options.header
            },
            success: (res) => {
                if (res.statusCode === 200) {
                    if (res.data.code === 200) {
                        resolve(res.data);
                    } else {
                        uni.showToast({
                            title: res.data.message || '请求失败',
                            icon: 'none'
                        });
                        reject(res.data);
                    }
                } else if (res.statusCode === 401) {
                    // 未授权，跳转登录
                    uni.reLaunch({
                        url: '/pages/login/login'
                    });
                    reject(res.data);
                } else {
                    uni.showToast({
                        title: '网络错误',
                        icon: 'none'
                    });
                    reject(res);
                }
            },
            fail: (err) => {
                uni.showToast({
                    title: '网络请求失败',
                    icon: 'none'
                });
                reject(err);
            }
        });
    });
}

/**
 * GET请求
 */
export function get(url, data = {}, options = {}) {
    return request({
        url,
        method: 'GET',
        data,
        ...options
    });
}

/**
 * POST请求
 */
export function post(url, data = {}, options = {}) {
    return request({
        url,
        method: 'POST',
        data,
        ...options
    });
}

/**
 * PUT请求
 */
export function put(url, data = {}, options = {}) {
    return request({
        url,
        method: 'PUT',
        data,
        ...options
    });
}

/**
 * DELETE请求
 */
export function del(url, data = {}, options = {}) {
    return request({
        url,
        method: 'DELETE',
        data,
        ...options
    });
}

export default request;
