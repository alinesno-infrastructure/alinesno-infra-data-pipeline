package com.alinesno.infra.data.pipeline.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import lombok.Data;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * TransferQueueEntity 类是任务列表实体类，用于表示任务的转移队列信息。
 * 它包含了转移名称、转移数据和状态等字段，用于记录任务的相关信息。
 */
@TableName("job_queue") // 指定数据库表名
@Data
public class JobQueueEntity extends InfraBaseEntity {

    /**
     * 转移名称
     */
    @TableField("job_name") // 指定数据库字段名
	@ColumnType(length=255)
	@ColumnComment("转移名称")
    private String jobName;

    /**
     * 任务ID
     */
    @TableField("job_id") // 指定数据库字段名
	@ColumnType(length=255)
	@ColumnComment("任务ID")
    private Long jobId ;

    /**
     * 转移数据
     */
    @TableField("job_data") // 指定数据库字段名
	@ColumnType(length=255)
	@ColumnComment("转移数据")
    private String jobData;

    /**
     * 状态
     */
    @TableField("status") // 指定数据库字段名
	@ColumnType(length=255)
	@ColumnComment("状态")
    private String status;

    /**
     * 当前执行到的步骤
     */
    @TableField("step") // 指定数据库字段名
	@ColumnType(length=255)
	@ColumnComment("当前执行到的步骤")
    private int step ;
}
