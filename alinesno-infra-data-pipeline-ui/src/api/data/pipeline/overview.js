import request from '@/utils/request'

/**
 * 驱动配置管理接口操作
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口前缀
const prefix = '/dbswitch/admin/api/v1/overview/'

// 接口配置项（和数据源管理接口规范对齐）
const managerUrl = {
  statistics: prefix + '/statistics',                     
}

export function getStatistics() {
  return request({
    url: managerUrl.statistics ,
    method: 'get'
  })
}

