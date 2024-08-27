package com.alinesno.infra.data.pipeline.api.dto;

import com.alinesno.infra.common.facade.base.BaseDto;
import com.alinesno.infra.data.pipeline.enums.JobStatusEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 功能名： 【请填写功能名称】
 * 数据表：  job
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobInstanceDto extends BaseDto {

    private Long jobId ; // 任务id
    private Long projectId ; // 所属项目
    private String jobName ; // 任务名称
    private String jobDesc ; // 任务描述
    private Long startTime ; // 开始时间
    private Long endTime ; // 结束时间
    private int countOrder = 0 ;
    private String status = JobStatusEnums.PROCESSING.getStatus() ; // 状态
    private String statusLabel ; // 状态描述
    private String errorMsg ; // 错误信息

    private String sourceDbType ; // 源数据库类型
    private String targetDbType ; // 目标数据库类型

    private long readerCount ; // 读取数量
    private long writerCount; // 写入数量

}
