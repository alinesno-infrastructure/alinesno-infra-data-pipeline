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

import com.alinesno.infra.data.pipeline.common.entity.PatternMapper;
import com.alinesno.infra.data.pipeline.common.entity.TableColumnPair;
import com.alinesno.infra.data.pipeline.common.type.CaseConvertEnum;
import com.alinesno.infra.data.pipeline.common.type.ProductTableEnum;
import com.alinesno.infra.data.pipeline.common.type.SyncOptionEnum;
import com.alinesno.infra.data.pipeline.handler.ListPatternHandler;
import com.alinesno.infra.data.pipeline.handler.ListTabColHandler;
import com.alinesno.infra.data.pipeline.handler.ListTypeHandler;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.EnumTypeHandler;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "DBSWITCH_ASSIGNMENT_CONFIG", autoResultMap = true)
public class AssignmentConfigEntity {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("assignment_id")
  private Long assignmentId;

  @TableField("source_connection_id")
  private Long sourceConnectionId;

  @TableField("source_schema")
  private String sourceSchema;

  @TableField(value = "table_type", typeHandler = EnumTypeHandler.class)
  private ProductTableEnum tableType;

  @TableField(value = "source_tables", typeHandler = ListTypeHandler.class)
  private List<String> sourceTables;

  @TableField(value = "table_incr_columns", typeHandler = ListTabColHandler.class)
  private List<TableColumnPair> incrTableColumns;

  @TableField("excluded_flag")
  private Boolean excludedFlag;

  @TableField("pre_sql_scripts")
  private String preSqlScripts;

  @TableField("post_sql_scripts")
  private String postSqlScripts;

  @TableField("target_connection_id")
  private Long targetConnectionId;

  @TableField("target_schema")
  private String targetSchema;

  @TableField(value = "table_name_case", typeHandler = EnumTypeHandler.class)
  private CaseConvertEnum tableNameCase;

  @TableField(value = "column_name_case", typeHandler = EnumTypeHandler.class)
  private CaseConvertEnum columnNameCase;

  @TableField(value = "table_name_map", typeHandler = ListPatternHandler.class)
  private List<PatternMapper> tableNameMap;

  @TableField(value = "column_name_map", typeHandler = ListPatternHandler.class)
  private List<PatternMapper> columnNameMap;

  @TableField("target_drop_table")
  private Boolean targetDropTable;

  @TableField("target_only_create")
  private Boolean targetOnlyCreate;

  @TableField("target_auto_increment")
  private Boolean targetAutoIncrement;

  @TableField(value = "target_sync_option", typeHandler = EnumTypeHandler.class)
  private SyncOptionEnum targetSyncOption;

  @TableField("before_sql_scripts")
  private String beforeSqlScripts;

  @TableField("after_sql_scripts")
  private String afterSqlScripts;

  @TableField("batch_size")
  private Integer batchSize;

  @TableField("channel_size")
  private Integer channelSize;

  @TableField("first_flag")
  private Boolean firstFlag;

  @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
  private Timestamp createTime;
}
