package com.alinesno.infra.data.pipeline.scheduler.dto;

import com.alinesno.infra.data.pipeline.datasource.MappingBean;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 任务信息
 */
@Data
public class TaskInfoDto implements Serializable {

    // taskInfo
    private String taskType ; // 任务类型
    private String name ;  // 任务名称
    private String describe ; // 任务描述

    // 上下文环境
    private TaskContext context ;

    // 配置
    private Settings settings ;

    // 读取端
    private SourceReader reader ;

    // 写入端
    private SinkWriter writer ;

    // 数据过滤插件
    private List<FilterPlugins> plugins ;

    // 字段映射关系
    private List<MappingBean> fieldMap ;

}
