/**
 * 运动相关API
 */
import { get, post, del } from '@/utils/request';

/**
 * 添加运动记录
 */
export function addSportRecord(data) {
    return post('/sport/record/add', data);
}

/**
 * 获取运动记录列表(分页)
 */
export function getSportRecordPage(params) {
    return get('/sport/record/page', params);
}

/**
 * 按日期范围获取运动记录
 */
export function getSportRecordsByDate(params) {
    return get('/sport/record/list', params);
}

/**
 * 获取运动统计数据
 */
export function getSportStatistics(params) {
    return get('/sport/record/statistics', params);
}

/**
 * 获取每日统计数据
 */
export function getDailyStatistics(params) {
    return get('/sport/record/daily-statistics', params);
}

/**
 * 删除运动记录
 */
export function deleteSportRecord(recordId) {
    return del(`/sport/record/delete/${recordId}`);
}
