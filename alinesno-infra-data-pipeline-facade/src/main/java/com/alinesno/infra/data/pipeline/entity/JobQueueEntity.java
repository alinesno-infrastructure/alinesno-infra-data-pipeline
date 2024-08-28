package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TransferQueueEntity 类是任务列表实体类，用于表示任务的转移队列信息。
 * 它包含了转移名称、转移数据和状态等字段，用于记录任务的相关信息。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pipeline_job_queue") // 指定数据库表名
@Table(comment = "任务列表")
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

}
