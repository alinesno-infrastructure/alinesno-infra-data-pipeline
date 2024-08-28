package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 功能名： 【请填写功能名称】
 * 数据表：  job
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
@TableName("pipeline_job")
@Table(comment = "任务")
public class JobEntity extends InfraBaseEntity {

    // 所属项目
    @TableField("project_id")
    @ColumnType(value = MySqlTypeConstant.INT)
    @ColumnComment("所属项目")
    private Long projectId;

    // 任务名称
    @TableField("job_name")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("任务名称")
    private String jobName;

    // 任务描述
    @TableField("job_desc")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("任务描述")
    private String jobDesc;

    // 状态(0:未发布,1:已发布,2:已停止,3:已删除)
    @TableField("status")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("状态")
    private String status;

    // 源数据库类型()
    @TableField("source_db_type")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("源数据库类型")
    private String sourceDbType;

    // 源数据库id
    @TableField("source_db_id")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("源数据库id")
    private long sourceDbId;

    // 源数据库批量读取数量
    @TableField("source_db_batch_size")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("源数据库批量读取数量")
    private int sourceDbBatchSize;

    // 目标数据库类型
    @TableField("target_db_type")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("目标数据库类型")
    private String targetDbType;

    // 目标数据库id
    @TableField("target_db_id")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("目标数据库id")
    private long targetDbId;

    // 目标数据库批量写入数量
    @TableField("target_db_batch_size")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("目标数据库批量写入数量")
    private int targetDbBatchSize;

    // 参与人(参与人获取到执行的状态)
    @TableField("participants")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("参与人")
    private String participants;

    /**
     * 关联的转换任务id
     */
    @TableField("trans_ids")
    private String transIds;

    /**
     * CRON定时任务策略
     */
    @TableField("job_cron")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("CRON定时任务策略")
    private String jobCron;

    @TableField("start_time")
    @ColumnType(value = MySqlTypeConstant.DATETIME)
    @ColumnComment("开始时间")
    private Date startTime ;

    @TableField("end_time")
    @ColumnType(value = MySqlTypeConstant.DATETIME)
    @ColumnComment("结束时间")
    private Date endTime ;

    /**
     * 作业文件路径
     */
    @TableField("relative_location")
    @ColumnType(value = MySqlTypeConstant.VARCHAR , length = 128)
    @ColumnComment("作业文件路径")
    private String relativeLocation;

    /**
     * 任务的上下文
     */
    @TableField("job_context")
    @ColumnType(value = MySqlTypeConstant.TEXT)
    @ColumnComment("任务上下文")
    private String jobContext ;

}
