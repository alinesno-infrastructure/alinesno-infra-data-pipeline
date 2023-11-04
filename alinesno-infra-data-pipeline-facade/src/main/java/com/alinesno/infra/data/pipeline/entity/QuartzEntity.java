package com.alinesno.infra.data.pipeline.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import lombok.Data;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 定时任务表实体类
 * 数据表：quartz
 * 表备注：用于存储定时任务的信息
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("quartz")
@Data
public class QuartzEntity extends InfraBaseEntity {

    /**
     * 任务描述
     */
    @TableField("quartz_description")
	@ColumnType(length=255)
	@ColumnComment("任务描述")
    private String quartzDescription;

    /**
     * 定时策略
     */
    @TableField("quartz_cron")
	@ColumnType(length=255)
	@ColumnComment("定时策略")
    private String quartzCron;
}
