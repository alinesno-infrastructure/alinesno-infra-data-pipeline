package com.alinesno.infra.data.pipeline.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import lombok.Data;
import java.util.Date;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("job_monitor")
@Data
public class JobMonitorEntity extends InfraBaseEntity {

    /**
     * 监控任务ID
     */
    @TableField("monitor_job_id")
	@ColumnType(length=255)
	@ColumnComment("监控任务ID")
    private String monitorJobId;

    /**
     * 监控成功次数
     */
    @TableField("monitor_success")
	@ColumnType(length=255)
	@ColumnComment("监控成功次数")
    private int monitorSuccess;

    /**
     * 监控失败次数
     */
    @TableField("monitor_fail")
	@ColumnType(length=255)
	@ColumnComment("监控失败次数")
    private int monitorFail;

    /**
     * 监控状态
     */
    @TableField("monitor_status")
	@ColumnType(length=255)
	@ColumnComment("监控状态")
    private int monitorStatus;

    /**
     * 运行状态
     */
    @TableField("run_status")
	@ColumnType(length=255)
	@ColumnComment("运行状态")
    private String runStatus;

    /**
     * 上次执行时间
     */
    @TableField("last_execute_time")
	@ColumnType(length=255)
	@ColumnComment("上次执行时间")
    private Date lastExecuteTime;

    /**
     * 下次执行时间
     */
    @TableField("next_execute_time")
	@ColumnType(length=255)
	@ColumnComment("下次执行时间")
    private Date nextExecuteTime;
}
