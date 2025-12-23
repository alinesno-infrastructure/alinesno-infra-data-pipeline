// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.model.request;

import com.alinesno.infra.data.pipeline.common.entity.PatternMapper;
import com.alinesno.infra.data.pipeline.common.entity.TableColumnPair;
import com.alinesno.infra.data.pipeline.common.type.CaseConvertEnum;
import com.alinesno.infra.data.pipeline.common.type.ProductTableEnum;
import com.alinesno.infra.data.pipeline.common.type.SyncOptionEnum;
import com.alinesno.infra.data.pipeline.entity.AssignmentConfigEntity;
import com.alinesno.infra.data.pipeline.type.IncludeExcludeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

public class AssigmentBaseRequest {

  @NoArgsConstructor
  @Data
  public static class AssigmentConfig {

    private Long sourceConnectionId;
    private String sourceSchema;
    private ProductTableEnum tableType;
    private IncludeExcludeEnum includeOrExclude;
    private List<String> sourceTables;
    private List<TableColumnPair> incrTableColumns;
    private String sourceBeforeSqlScripts;
    private String sourceAfterSqlScripts;

    private Long targetConnectionId;
    private String targetSchema;
    private CaseConvertEnum tableNameCase;
    private CaseConvertEnum columnNameCase;
    private List<PatternMapper> tableNameMapper;
    private List<PatternMapper> columnNameMapper;
    private Boolean targetDropTable;
    private Boolean targetOnlyCreate;
    private Boolean targetAutoIncrement;
    private SyncOptionEnum targetSyncOption;
    private String targetBeforeSqlScripts;
    private String targetAfterSqlScripts;
    private Integer batchSize;
    private Integer channelSize;
  }

  protected AssignmentConfigEntity toAssignmentConfig(final Long assignmentId, final AssigmentConfig config) {
    AssignmentConfigEntity assignmentConfigEntity = new AssignmentConfigEntity();
    assignmentConfigEntity.setAssignmentId(assignmentId);
    assignmentConfigEntity.setSourceConnectionId(config.getSourceConnectionId());
    assignmentConfigEntity.setSourceSchema(config.getSourceSchema());
    assignmentConfigEntity.setTableType(config.getTableType());
    assignmentConfigEntity.setSourceTables(config.getSourceTables());
    assignmentConfigEntity.setIncrTableColumns(config.getIncrTableColumns());
    assignmentConfigEntity.setExcludedFlag(getExcludedFlag(config.getIncludeOrExclude()));
    assignmentConfigEntity.setPreSqlScripts(getTrimValueOrNull(config.getSourceBeforeSqlScripts()));
    assignmentConfigEntity.setPostSqlScripts(getTrimValueOrNull(config.getSourceAfterSqlScripts()));
    assignmentConfigEntity.setTargetConnectionId(config.getTargetConnectionId());
    assignmentConfigEntity.setTargetSchema(config.getTargetSchema());
    assignmentConfigEntity.setTableNameCase(config.getTableNameCase());
    assignmentConfigEntity.setColumnNameCase(config.getColumnNameCase());
    assignmentConfigEntity.setTableNameMap(config.getTableNameMapper());
    assignmentConfigEntity.setColumnNameMap(config.getColumnNameMapper());
    assignmentConfigEntity.setTargetDropTable(config.getTargetDropTable());
    assignmentConfigEntity.setTargetOnlyCreate(config.getTargetOnlyCreate());
    assignmentConfigEntity.setTargetAutoIncrement(config.getTargetAutoIncrement());
    assignmentConfigEntity.setBeforeSqlScripts(getTrimValueOrNull(config.getTargetBeforeSqlScripts()));
    assignmentConfigEntity.setAfterSqlScripts(getTrimValueOrNull(config.getTargetAfterSqlScripts()));
    assignmentConfigEntity.setTargetSyncOption(config.getTargetSyncOption());
    assignmentConfigEntity.setBatchSize(getValueOrDefault(config.getBatchSize(), 10000));
    assignmentConfigEntity.setChannelSize(getValueOrDefault(config.getChannelSize(), 100));
    assignmentConfigEntity.setFirstFlag(Boolean.FALSE);

    return assignmentConfigEntity;
  }

  protected boolean getExcludedFlag(IncludeExcludeEnum includeOrExclude) {
    return includeOrExclude == IncludeExcludeEnum.EXCLUDE;
  }

  protected int getValueOrDefault(Integer value, int defaultValue) {
    return Objects.nonNull(value) ? value : defaultValue;
  }

  protected String getTrimValueOrNull(String value) {
    return Objects.nonNull(value) ? value.trim() : null;
  }
}
