package com.alinesno.infra.data.pipeline.entity;

import java.util.Date;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pipeline_transform_history")
@Table(comment = "转换运行历史")
public class TransformRunHisEntity extends InfraBaseEntity {

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

}
