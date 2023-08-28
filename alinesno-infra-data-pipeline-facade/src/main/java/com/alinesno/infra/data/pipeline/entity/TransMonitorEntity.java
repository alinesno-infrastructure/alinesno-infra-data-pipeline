package com.alinesno.infra.data.pipeline.entity;

import java.util.Date;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("trans_monitor")
public class TransMonitorEntity extends InfraBaseEntity {

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

    /**
     * 获取监控转换ID
     */
    public String getMonitorTransid() {
        return monitorTransid;
    }

    /**
     * 设置监控转换ID
     */
    public void setMonitorTransid(String monitorTransid) {
        this.monitorTransid = monitorTransid;
    }

    /**
     * 获取监控成功次数
     */
    public int getMonitorSuccess() {
        return monitorSuccess;
    }

    /**
     * 设置监控成功次数
     */
    public void setMonitorSuccess(int monitorSuccess) {
        this.monitorSuccess = monitorSuccess;
    }

    /**
     * 获取监控失败次数
     */
    public int getMonitorFail() {
        return monitorFail;
    }

    /**
     * 设置监控失败次数
     */
    public void setMonitorFail(int monitorFail) {
        this.monitorFail = monitorFail;
    }

    /**
     * 获取监控状态
     */
    public int getMonitorStatus() {
        return monitorStatus;
    }

    /**
     * 设置监控状态
     */
    public void setMonitorStatus(int monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    /**
     * 获取运行状态
     */
    public String getRunStatus() {
        return runStatus;
    }

    /**
     * 设置运行状态
     */
    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    /**
     * 获取上次执行时间
     */
    public Date getLastExecuteTime() {
        return lastExecuteTime;
    }

    /**
     * 设置上次执行时间
     */
    public void setLastExecuteTime(Date lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    /**
     * 获取下次执行时间
     */
    public Date getNextExecuteTime() {
        return nextExecuteTime;
    }

    /**
     * 设置下次执行时间
     */
    public void setNextExecuteTime(Date nextExecuteTime) {
        this.nextExecuteTime = nextExecuteTime;
    }
}
