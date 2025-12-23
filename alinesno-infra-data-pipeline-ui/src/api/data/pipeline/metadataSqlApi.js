// src/api/metadataSqlApi.js
import request from "@/utils/request";

/**
 * 元数据&SQL在线 相关接口
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口前缀
const prefix = "/dbswitch/admin/api/v1";

/**
 * 加载数据源列表
 * @returns {Promise}
 */
export const loadConnectionsApi = () => {
  return request({
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    url: `${prefix}/connection/list/name`,
  });
};

/**
 * 加载数据源模式列表（树一级节点）
 * @param {Number} dataSourceId 数据源ID
 * @returns {Promise}
 */
export const loadSchemasApi = (dataSourceId) => {
  return request({
    method: "GET",
    url: `${prefix}/connection/schemas/get/${dataSourceId}`,
  });
};

/**
 * 加载表/视图列表（树二级节点）
 * @param {Number} dataSourceId 数据源ID
 * @param {String} schema 模式名
 * @param {String} type TABLE/VIEW
 * @returns {Promise}
 */
export const loadTablesListApi = (dataSourceId, schema, type) => {
  const tableType = type === "VIEW" ? "views" : "tables";
  return request({
    method: "GET",
    url: `${prefix}/connection/${tableType}/get/${dataSourceId}?schema=${encodeURIComponent(schema)}`,
  });
};

/**
 * 获取表元数据
 * @param {Number} id 数据源ID
 * @param {String} schema 模式名
 * @param {String} table 表名
 * @returns {Promise}
 */
export const getTableMetaApi = (id, schema, table) => {
  return request({
    method: "GET",
    url: `${prefix}/metadata/meta/table/${id}?schema=${encodeURIComponent(schema)}&table=${encodeURIComponent(table)}`,
  });
};

/**
 * 获取表取样数据
 * @param {Number} id 数据源ID
 * @param {String} schema 模式名
 * @param {String} table 表名
 * @returns {Promise}
 */
export const getTableDataApi = (id, schema, table) => {
  return request({
    method: "GET",
    url: `${prefix}/metadata/data/table/${id}?schema=${encodeURIComponent(schema)}&table=${encodeURIComponent(table)}`,
  });
};

/**
 * 执行SQL脚本
 * @param {Number} dataSourceId 数据源ID
 * @param {String} sqlScript SQL脚本
 * @param {Number} page 页码
 * @param {Number} size 每页条数
 * @returns {Promise}
 */
export const executeSqlScriptApi = (dataSourceId, sqlScript, page, size) => {
  return request({
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    url: `${prefix}/metadata/data/sql/${dataSourceId}`,
    data: {
      script: sqlScript,
      page,
      size,
    },
  });
};