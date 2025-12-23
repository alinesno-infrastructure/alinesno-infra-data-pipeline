// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.entity;

import com.alinesno.infra.data.pipeline.type.ScheduleModeEnum;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "DBSWITCH_ASSIGNMENT_TASK", autoResultMap = true)
public class AssignmentTaskEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("name")
  private String name;

  @TableField(value = "description")
  private String description;

  @TableField(value = "schedule_mode", typeHandler = EnumTypeHandler.class)
  private ScheduleModeEnum scheduleMode;

  @TableField(value = "cron_expression", jdbcType = JdbcType.LONGVARCHAR, insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
  private String cronExpression;

  @TableField("published")
  private Boolean published;

  @TableField("content")
  private String content;

  @TableField("job_key")
  private String jobKey;

  @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp createTime;

  @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp updateTime;
}
