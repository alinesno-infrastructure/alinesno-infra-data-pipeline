import request from '@/utils/request'

/**
 * 数据源管理接口操作
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/dbswitch/admin/api/v1/connection'
var managerUrl = {
  list: prefix + '/list',
  types: prefix + '/types',
  test: prefix + '/test',
  delete: prefix + '/delete',
  typeDetail: prefix + '/type/',      // 拼接数据库类型使用
  connectionDetail: prefix + '/get/', // 拼接数据源ID使用
  drivers: prefix + '/',              // 拼接数据库类型 + /drivers 使用
  preTest: prefix + '/preTest',       // 测试连接
  create: prefix + '/create',         // 创建数据源
  update: prefix + '/update'          // 更新数据源
}

// 分页查询数据源列表
export function listDataSource(query) {
  return request({
    url: managerUrl.list,
    method: 'post',
    data: query
  })
}

// 获取支持的数据库类型
export function getDatabaseTypes() {
  return request({
    url: managerUrl.types,
    method: 'get'
  })
}

// 测试数据源连接
export function testDataSource(id) {
  return request({
    url: managerUrl.test + '/' + id,
    method: 'get'
  })
}

// 删除数据源
export function deleteDataSource(id) {
  return request({
    url: managerUrl.delete + '/' + id,
    method: 'delete'
  })
}


// 获取数据库类型详情
export function getDatabaseTypeDetail(type) {
  return request({
    url: managerUrl.typeDetail + type,
    method: 'get'
  })
}

// 获取数据源连接详情
export function getConnectionDetail(id) {
  return request({
    url: managerUrl.connectionDetail + id,
    method: 'get'
  })
}

// 获取数据库驱动版本列表
export function getConnectionDrivers(type) {
  return request({
    url: managerUrl.drivers + type + '/drivers',
    method: 'get'
  })
}

// 测试数据源连接
export function preTestConnection(data) {
  return request({
    url: managerUrl.preTest,
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: data
  })
}

// 创建数据源
export function createDataSource(data) {
  return request({
    url: managerUrl.create,
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: data
  })
}

// 更新数据源
export function updateDataSource(data) {
  return request({
    url: managerUrl.update,
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data: data
  })
}