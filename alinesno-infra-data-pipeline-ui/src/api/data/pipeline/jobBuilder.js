import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

/**
 * 数据库接口操作
 *
 * @author luoxiaodong
 * @since 1.0.0
 */

// 接口配置项
var prefix = '/api/infra/data/pipeline/jobBuilder/' ;
var managerUrl = {
    getAllSourceReader: prefix +"getAllSourceReader" ,
    getAllPlugin: prefix + 'getAllPlugin' ,
}

// 获取到所有的Reader列表和Sink列表
export function getAllSourceReader(){
  return request({
    url: managerUrl.getAllSourceReader,
    method: 'get'
  })
}

// 获取到所有插件的列表
export function getAllPlugin(){
  return request({
    url: managerUrl.getAllPlugin,
    method: 'get'
  })
}
