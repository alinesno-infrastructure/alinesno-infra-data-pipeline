import request from '@/utils/request'

/**
 * 驱动配置管理接口操作
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口前缀
const prefix = '/dbswitch/admin/api/v1/connection'

// 接口配置项（和数据源管理接口规范对齐）
const managerUrl = {
  types: prefix + '/types',                     // 数据库产品类型列表
  drivers: prefix + '/',                        // 驱动列表（需拼接 类型 + /drivers）
}

/**
 * 获取数据库连接类型列表
 */
export function getConnectionTypes() {
  return request({
    url: managerUrl.types,
    method: 'get'
  })
}

/**
 * 根据数据库类型获取驱动列表
 * @param {string} type 数据库类型（如MYSQL）
 */
export function getDriverByType(type) {
  // 拼接规则：drivers前缀 + 数据库类型 + /drivers
  return request({
    url: `${managerUrl.drivers}${type}/drivers`,
    method: 'get'
  })
}