package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 功能名：数据抽取任务监控
 * 数据表：job_monitor
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
@TableName("pipeline_job_monitor")
public class JobMonitorEntity extends InfraBaseEntity {

    /**
     * 监控任务ID
     */
    @TableField("monitor_job_id")
    private String monitorJobId;

    /**
     * 监控成功次数
     */
    @TableField("monitor_success")
    private int monitorSuccess;

    /**
     * 监控失败次数
     */
    @TableField("monitor_fail")
    private int monitorFail;

    /**
     * 监控状态
     */
    @TableField("monitor_status")
    private int monitorStatus;

    /**
     * 运行状态
     */
    @TableField("run_status")
    private String runStatus;

    /**
     * 上次执行时间
     */
    @TableField("last_execute_time")
    private Date lastExecuteTime;

    /**
     * 下次执行时间
     */
    @TableField("next_execute_time")
    private Date nextExecuteTime;

}
