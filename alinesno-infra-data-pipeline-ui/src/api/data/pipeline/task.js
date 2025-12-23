// src/api/taskApi.js
import request from "@/utils/request";

/**
 * 任务管理相关接口
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口前缀
const prefix = "/dbswitch/admin/api/v1";

/**
 * 获取任务列表
 * @param {Object} params - 查询参数
 * @param {string} params.searchText - 搜索关键字
 * @param {number} params.page - 当前页码
 * @param {number} params.size - 每页条数
 * @returns {Promise}
 */
export function getTaskList(params) {
  return request({
    url: `${prefix}/assignment/list`,
    method: "post",
    data: JSON.stringify(params),
    headers: {
      "Content-Type": "application/json",
    },
  });
}

/**
 * 批量启动任务
 * @param {Array|string} ids - 任务ID集合（数组或拼接后的字符串）
 * @returns {Promise}
 */
export function batchStartTask(ids) {
  const idsStr = Array.isArray(ids) ? ids.join(",") : ids;
  return request({
    url: `${prefix}/assignment/deploy?ids=${idsStr}`,
    method: "post",
    headers: {
      "Content-Type": "application/json",
    },
  });
}

/**
 * 批量停止任务
 * @param {Array|string} ids - 任务ID集合（数组或拼接后的字符串）
 * @returns {Promise}
 */
export function batchStopTask(ids) {
  const idsStr = Array.isArray(ids) ? ids.join(",") : ids;
  return request({
    url: `${prefix}/assignment/retire?ids=${idsStr}`,
    method: "post",
    headers: {
      "Content-Type": "application/json",
    },
  });
}

/**
 * 批量导出任务
 * @param {Array|string} ids - 任务ID集合（数组或拼接后的字符串）
 * @returns {Promise}
 */
export function exportTask(ids) {
  const idsStr = Array.isArray(ids) ? ids.join(",") : ids;
  return request({
    url: `${prefix}/assignment/export?ids=${idsStr}`,
    method: "post",
    responseType: "blob",
    headers: {
      "Content-Type": "application/json",
    },
  });
}

/**
 * 删除任务
 * @param {number|string} id - 任务ID
 * @returns {Promise}
 */
export function deleteTask(id) {
  return request({
    url: `${prefix}/assignment/delete/${id}`,
    method: "delete",
  });
}

/**
 * 启动单个任务
 * @param {number|string} id - 任务ID
 * @returns {Promise}
 */
export function publishTask(id) {
  return request({
    url: `${prefix}/assignment/deploy?ids=${id}`,
    method: "post",
    headers: {
      "Content-Type": "application/json",
    },
  });
}

/**
 * 停止单个任务
 * @param {number|string} id - 任务ID
 * @returns {Promise}
 */
export function retireTask(id) {
  return request({
    url: `${prefix}/assignment/retire?ids=${id}`,
    method: "post",
    headers: {
      "Content-Type": "application/json",
    },
  });
}

/**
 * 手动执行任务
 * @param {number|string} id - 任务ID
 * @returns {Promise}
 */
export function runTask(id) {
  return request({
    url: `${prefix}/assignment/run`,
    method: "post",
    data: JSON.stringify([id]),
    headers: {
      "Content-Type": "application/json",
    },
  });
}

/**
 * 获取连接名称列表
 */
export function getConnectionList() {
  return request({
    url: '/dbswitch/admin/api/v1/connection/list/name',
    method: 'GET'
  })
}

/**
 * 获取任务详情
 * @param {Number} id 任务ID
 */
export function getAssignmentDetail(id) {
  return request({
    url: `/dbswitch/admin/api/v1/assignment/detail/id/${id}`,
    method: 'GET'
  })
}

/**
 * 获取连接的Schema列表
 * @param {Number} id 连接ID
 */
export function getConnectionSchemas(id) {
  return request({
    url: `/dbswitch/admin/api/v1/connection/schemas/get/${id}`,
    method: 'GET'
  })
}

/**
 * 获取连接的表列表
 * @param {Number} id 连接ID
 * @param {String} schema Schema名称
 */
export function getConnectionTables(id, schema) {
  return request({
    url: `/dbswitch/admin/api/v1/connection/tables/get/${id}`,
    method: 'GET',
    params: { schema }
  })
}

/**
 * 获取连接的视图列表
 * @param {Number} id 连接ID
 * @param {String} schema Schema名称
 */
export function getConnectionViews(id, schema) {
  return request({
    url: `/dbswitch/admin/api/v1/connection/views/get/${id}`,
    method: 'GET',
    params: { schema }
  })
}

/**
 * 预览表名映射
 * @param {Object} data 请求参数
 */
export function previewTableMapper(data) {
  return request({
    url: '/dbswitch/admin/api/v1/mapper/preview/table',
    method: 'POST',
    data
  })
}

/**
 * 预览列名映射
 * @param {Object} data 请求参数
 */
export function previewColumnMapper(data) {
  return request({
    url: '/dbswitch/admin/api/v1/mapper/preview/column',
    method: 'POST',
    data
  })
}

/**
 * 创建任务
 * @param {Object} data 任务数据
 */
export function createAssignment(data) {
  return request({
    url: '/dbswitch/admin/api/v1/assignment/create',
    method: 'POST',
    data
  })
}

/**
 * 更新任务
 * @param {Object} data 任务数据
 */
export function updateAssignment(data) {
  return request({
    url: '/dbswitch/admin/api/v1/assignment/update',
    method: 'POST',
    data
  })
}

/**
 * 获取任务详情
 * @param {number} taskId 任务ID
 * @returns {Promise}
 */
export const getTaskDetail = (taskId) => {
  return request({
    url: `/api/task/detail/${taskId}`,
    method: 'get'
  });
};

// /**
//  * 获取任务列表
//  * @param {Object} params 查询参数
//  * @returns {Promise}
//  */
// export const getTaskList = (params) => {
//   return request({
//     url: '/api/task/list',
//     method: 'get',
//     params
//   });
// };

/**
 * 更新任务配置
 * @param {Object} data 任务数据
 * @returns {Promise}
 */
export const updateTask = (data) => {
  return request({
    url: `/api/task/update/${data.id}`,
    method: 'post',
    data
  });
};


/**
 * 取消作业执行
 * @param {number} jobId 作业ID
 * @returns {Promise}
 */
export function cancelJob(jobId) {
  return request({
    url: `${prefix}/ops/job/cancel`,
    method: "GET",
    params: { id: jobId }
  });
}

/**
 * 获取任务基本信息
 * @param {number} taskId 任务ID
 * @returns {Promise}
 */
export function getAssignmentInfo(taskId) {
  return request({
    url: `${prefix}/assignment/info/id/${taskId}`,
    method: "GET"
  });
}

/**
 * 获取任务作业列表
 * @param {number} taskId 任务ID
 * @param {number} page 页码
 * @param {number} size 每页条数
 * @returns {Promise}
 */
export function getJobList(taskId, page, size) {
  return request({
    url: `${prefix}/ops/jobs/list/${page}/${size}`,
    method: "GET",
    params: { id: taskId }
  });
}

/**
 * 获取作业日志（尾部）
 * @param {number} jobId 作业ID
 * @param {number} size 日志条数
 * @returns {Promise}
 */
export function getJobLogsTail(jobId, size) {
  return request({
    url: `${prefix}/ops/job/logs/tail`,
    method: "GET",
    params: { id: jobId, size }
  });
}

/**
 * 获取作业后续日志
 * @param {number} jobId 作业ID
 * @param {number} baseId 日志起始ID
 * @returns {Promise}
 */
export function getJobLogsNext(jobId, baseId) {
  return request({
    url: `${prefix}/ops/job/logs/next`,
    method: "GET",
    params: { id: jobId, baseId }
  });
}