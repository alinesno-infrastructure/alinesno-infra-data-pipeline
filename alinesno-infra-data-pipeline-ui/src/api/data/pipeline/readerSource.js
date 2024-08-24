import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/**
 * 数据库接口操作
 * 
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/api/infra/data/pipeline/readerSource/' ;
var managerUrl = {
    datatables : prefix +"datatables" ,
    createUrl: prefix + 'add' ,
    saveUrl: prefix + 'saveDb' ,
    updateUrl: prefix +"modifyDb" ,
    statusUrl: prefix +"changeStatus" ,
    cleanUrl: prefix + "clean",
    detailUrl: prefix +"detail",
    removeUrl: prefix + "delete" ,
    exportUrl: prefix + "exportExcel",
    changeField: prefix + "changeField",
    downloadfile: prefix + "downloadfile",
    allReaderSource: prefix + "allReaderSource",
    fetchData: prefix + "fetchData",
    fetchFieldMeta: prefix + "fetchFieldMeta",
    fetchTableData: prefix + "fetchTableData",
    checkDBUrl: prefix + "checkDB",
}

// 获取到字段元数据
export function fetchFieldMeta(data){
  return request({
    url: managerUrl.fetchFieldMeta,
    method: 'post',
    data: data
  })
}

// 获取到数据源的表结构 
export function fetchTableData(sourceId) {
  return request({
    url: managerUrl.fetchTableData + "?sourceId=" + parseStrEmpty(sourceId),
    method: 'get',
  })
}

// 数据库返回结果操作
export function fetchData(data) {
  return request({
    url: managerUrl.fetchData,
    method: 'post',
    data: data
  })
}

// 获取到所有数据源库
export function allReaderSource(){
  return request({
    url: managerUrl.allReaderSource , 
    method: 'get'
  })
}

//检查配置
export function checkDbConfig(data) {
  return request({
    url: managerUrl.checkDBUrl,
    method: 'post',
    data: data
  })
}

// 查询数据库列表
export function listReaderSource(query) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query
  })
}

// 查询数据库详细
export function getReaderSource(databaseId) {
  return request({
    url: managerUrl.detailUrl + '/' + parseStrEmpty(databaseId),
    method: 'get'
  })
}

// 新增数据库
export function addReaderSource(data) {
  return request({
    url: managerUrl.saveUrl ,
    method: 'post',
    data: data
  })
}

// 修改数据库
export function updateReaderSource(data) {
  return request({
    url: managerUrl.updateUrl ,
    method: 'put',
    data: data
  })
}

// 删除数据库
export function delReaderSource(databaseId) {
  return request({
    url: managerUrl.removeUrl + '/' + parseStrEmpty(databaseId),
    method: 'delete'
  })
}
