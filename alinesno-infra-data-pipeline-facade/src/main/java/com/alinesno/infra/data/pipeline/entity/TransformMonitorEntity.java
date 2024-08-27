package com.alinesno.infra.data.pipeline.entity;

import java.util.Date;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 转换监控实体类
 * 数据表：trans_monitor
 * 表备注：用于存储转换监控信息
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pipeline_transform_monitor")
@Table(comment = "转换监控")
public class TransformMonitorEntity extends TransformEntity {

    /**
     * 监控转换ID
     */
    @TableField("monitor_transid")
    private String monitorTransid;

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
