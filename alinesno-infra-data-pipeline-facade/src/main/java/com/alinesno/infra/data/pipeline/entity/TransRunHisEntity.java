package com.alinesno.infra.data.pipeline.entity;

import java.util.Date;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 转换运行历史实体类
 * 数据表：trans_run_his
 * 表备注：用于存储转换运行历史信息
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("trans_run_his")
public class TransRunHisEntity extends InfraBaseEntity {

    /**
     * 转换ID
     */
    @TableField("monitor_trans_id")
    private String monitorTransId;

    /**
     * 执行开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 执行结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 转换执行结果,0:失败；1:成功
     */
    @TableField("run_status")
    private int runStatus;

    /**
     * 转换执行日志
     */
    @TableField("remark")
    private String remark;

    /**
     * 获取转换ID
     */
    public String getMonitorTransId() {
        return monitorTransId;
    }

    /**
     * 设置转换ID
     */
    public void setMonitorTransId(String monitorTransId) {
        this.monitorTransId = monitorTransId;
    }

    /**
     * 获取执行开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置执行开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取执行结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置执行结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取转换执行结果
     */
    public int getRunStatus() {
        return runStatus;
    }

    /**
     * 设置转换执行结果
     */
    public void setRunStatus(int runStatus) {
        this.runStatus = runStatus;
    }

    /**
     * 获取转换执行日志
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置转换执行日志
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
