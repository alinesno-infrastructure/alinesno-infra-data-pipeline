package com.alinesno.infra.data.pipeline.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import lombok.Data;
import java.util.Date;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("job_run_his")
@Data
public class JobRunHisEntity extends BaseKettleEntity {

    /**
     * 作业ID
     */
    @TableField("run_job_id")
	@ColumnType(length=255)
	@ColumnComment("作业ID")
    private String runJobId;

    /**
     * 执行开始时间
     */
    @TableField("start_time")
	@ColumnType(length=255)
	@ColumnComment("执行开始时间")
    private Date startTime;

    /**
     * 执行结束时间
     */
    @TableField("end_time")
	@ColumnType(length=255)
	@ColumnComment("执行结束时间")
    private Date endTime;

    /**
     * 作业执行结果,0:失败；1:成功
     */
    @TableField("run_status")
	@ColumnType(length=255)
	@ColumnComment("作业执行结果,0:失败；1:成功")
    private int runStatus;

    /**
     * 作业执行日志
     */
    @TableField("remark")
	@ColumnType(length=255)
	@ColumnComment("作业执行日志")
    private String remark;
}
