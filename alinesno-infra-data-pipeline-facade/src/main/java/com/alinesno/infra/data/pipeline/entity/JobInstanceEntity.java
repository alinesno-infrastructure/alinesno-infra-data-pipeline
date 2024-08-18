package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.alinesno.infra.data.pipeline.enums.JobStatusEnums;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
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
@TableName("pipeline_job_instance")
public class JobInstanceEntity extends InfraBaseEntity {

    @TableField("job_id")
    @ColumnType
    @ColumnComment("任务id")
    private Long jobId ; // 任务id

    @TableField("project_id")
    @ColumnType
    @ColumnComment("所属项目")
    private Long projectId ; // 所属项目

    @TableField("job_name")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("任务名称")
    private String jobName ; // 任务名称

    @TableField("start_time")
    @ColumnType
    @ColumnComment("开始时间")
    private Long startTime ; // 开始时间

    @TableField("end_time")
    @ColumnType
    @ColumnComment("结束时间")
    private Long endTime ; // 结束时间

    @TableField("status")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 16)
    @ColumnComment("状态")
    private String status = JobStatusEnums.PROCESSING.getStatus() ; // 状态

    @TableField("error_msg")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 256)
    @ColumnComment("错误信息")
    private String errorMsg ; // 错误信息

}
