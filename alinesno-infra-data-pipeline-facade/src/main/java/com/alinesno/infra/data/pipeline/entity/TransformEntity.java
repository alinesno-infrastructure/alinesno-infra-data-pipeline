package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 转换实体类
 * 数据表：trans
 * 表备注：存储转换信息
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pipeline_transform")
@Table(comment = "转换")
public class TransformEntity extends InfraBaseEntity {

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 路径
     */
    @TableField("path")
    private String path;

    /**
     * Quartz
     */
    @TableField("quartz")
    private String quartz;

    /**
     * 同步策略
     */
    @TableField("strategy")
    private String syncStrategy;

    /**
     * 日志级别
     */
    @TableField("log_level")
    private String logLevel;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private String categoryId;


    /**
     * 仓库名称
     */
    @TableField("git_id")
    private String gitId;

    /**
     * 转换文件路径
     */
    @TableField("relative_location")
    private String relativeLocation;

    /**
     * 任务的上下文
     */
    @TableField("trans_context")
    private String transContext ;

    /**
     * 关联的任务id
     */
    @TableField("job_id")
    private Long jobId ;

    @TableField("job_instance_id")
    private Long jobInstanceId ;

    /**
     * 任务排序
     */
    @TableField("order_step")
    private int orderStep ;

    /**
     * 处理完成的数据
     */
    @TableField("process_data_count")
    private Long processDataCount ;

    /**
     * 需要处理的数据量
     */
    @TableField("total_data_count")
    private Long totalDataCount ;
}
