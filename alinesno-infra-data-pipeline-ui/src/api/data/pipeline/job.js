import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/**
 * 数据库接口操作
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/api/infra/data/pipeline/job/' ;
var managerUrl = {
    datatables : prefix +"datatables" ,
    createUrl: prefix + 'add' ,
    saveUrl: prefix + 'save' ,
    updateUrl: prefix +"modify" ,
    statusUrl: prefix +"changeStatus" ,
    cleanUrl: prefix + "clean",
    detailUrl: prefix +"detail",
    removeUrl: prefix + "delete" ,
    exportUrl: prefix + "exportExcel",
    runOneTime: prefix + "runOneTime", // 立即执行一次job
    pauseTrigger: prefix + "pauseTrigger", // 暂停trigger
    resumeTrigger: prefix + "resumeTrigger", // 恢复trigger 
    deleteJob: prefix + "deleteJob", // 删除job
    updateJob: prefix + "updateJob", // 更新job
    changeField: prefix + "changeField",
    listAllJob: prefix + "listAllJob",
    commitJob: prefix + "commitJob", // 保存任务
    catalogTreeSelect: prefix + "catalogTreeSelect",
    downloadfile: prefix + "downloadfile"
}

// 保存任务
export function commitJob(data){
  return request({
    url: managerUrl.commitJob ,
    method: 'post',
    data: data
  })
}

// 查询部门下拉树结构
export function catalogTreeSelect() {
  return request({
    url: managerUrl.catalogTreeSelect ,
    method: 'get'
  })
}

// 立即执行一次job
export function runOneTime(jobId){
  return request({
    url: managerUrl.runOneTime + "?jobId=" + parseStrEmpty(jobId),
    method: 'post',
  })
}

// 暂停trigger
export function pauseTrigger(jobId){
  return request({
    url: managerUrl.pauseTrigger + "?jobId=" + parseStrEmpty(jobId),
    method: 'post',
  })
}

// 恢复trigger
export function resumeTrigger(jobId){
  return request({
    url: managerUrl.resumeTrigger + "?jobId=" + parseStrEmpty(jobId),
    method: 'post',
  })
}

// 删除job
export function deleteJob(jobId){
  return request({
    url: managerUrl.deleteJob + "?jobId=" + parseStrEmpty(jobId),
    method: 'post',
  })
}

// 更新job
// export function updateJob(data){
//   return request({
//     url: managerUrl.updateJob ,
//     method: 'post',
//     data: data
//   })
// }

// 列出所有集成渠道
export function listAllJob(){
  return request({
    url: managerUrl.listAllJob ,
    method: 'get'
  })
}

// 修改字段
export function changStatusField(data){
  return request({
    url: managerUrl.changeField ,
    method: 'post',
    data: data
  })
}

// 查询数据库列表
export function listJob(query) {
  return request({
    url: managerUrl.datatables ,
    method: 'post',
    params: query
  })
}

// 查询数据库详细
export function getJob(databaseId) {
  return request({
    url: managerUrl.detailUrl + '/' + parseStrEmpty(databaseId),
    method: 'get'
  })
}

// 新增数据库
export function addJob(data) {
  return request({
    url: managerUrl.saveUrl ,
    method: 'post',
    data: data
  })
}

// 修改数据库
export function updateJob(data) {
  return request({
    url: managerUrl.updateUrl ,
    method: 'put',
    data: data
  })
}

// 删除数据库
export function delJob(databaseId) {
  return request({
    url: managerUrl.removeUrl + '/' + parseStrEmpty(databaseId),
    method: 'delete'
  })
}
