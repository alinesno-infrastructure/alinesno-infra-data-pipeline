import request from '@/utils/request'

/**
 * 系统日志管理接口操作
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口前缀
const prefix = '/dbswitch/admin/api/v1/syslog'

// 接口配置项
const managerUrl = {
  list: prefix + '/list/' // 需拼接：类型/页码/页大小
}

/**
 * 获取系统日志列表
 * @param {number} type 日志类型（固定传2）
 * @param {number} page 页码
 * @param {number} size 页大小
 */
export function getSysLogList(type, page, size) {
  return request({
    url: `${managerUrl.list}${type}/${page}/${size}`,
    method: 'get'
  })
}