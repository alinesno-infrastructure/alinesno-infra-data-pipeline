package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 功能名：作业执行历史记录实体类
 * 数据表：job_run_his
 * 表备注：用于记录作业执行的历史信息
 * 
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pipeline_job_run_his")
@Table(comment = "作业执行历史记录")
public class JobRunHisEntity extends InfraBaseEntity {

    /**
     * 作业ID
     */
    @TableField("run_job_id")
    private String runJobId;

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
     * 作业执行结果,0:失败；1:成功
     */
    @TableField("run_status")
    private int runStatus;

    /**
     * 作业执行日志
     */
    @TableField("remark")
    private String remark;

}
