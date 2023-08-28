package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * TransferQueueEntity 类是任务列表实体类，用于表示任务的转移队列信息。
 * 它包含了转移名称、转移数据和状态等字段，用于记录任务的相关信息。
 */
@TableName("job_queue") // 指定数据库表名
public class JobQueueEntity extends InfraBaseEntity {

    /**
     * 转移名称
     */
    @TableField("job_name") // 指定数据库字段名
    private String jobName;

    /**
     * 任务ID
     */
    @TableField("job_id") // 指定数据库字段名
    private Long jobId ;

    /**
     * 转移数据
     */
    @TableField("job_data") // 指定数据库字段名
    private String jobData;

    /**
     * 状态
     */
    @TableField("status") // 指定数据库字段名
    private String status;

    /**
     * 当前执行到的步骤
     */
    @TableField("step") // 指定数据库字段名
    private int step ;

    // 省略 getter 和 setter 方法

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobData() {
        return jobData;
    }

    public void setJobData(String jobData) {
        this.jobData = jobData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
