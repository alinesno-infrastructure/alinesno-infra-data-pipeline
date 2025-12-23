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

import com.alinesno.infra.data.pipeline.common.exception.DbswitchException;
import com.alinesno.infra.data.pipeline.common.response.ResultCode;
import com.alinesno.infra.data.pipeline.common.util.PatterNameUtils;
import com.alinesno.infra.data.pipeline.entity.AssignmentConfigEntity;
import com.alinesno.infra.data.pipeline.entity.AssignmentTaskEntity;
import com.alinesno.infra.data.pipeline.type.ScheduleModeEnum;
import com.alinesno.infra.data.pipeline.util.CronExprUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

@NoArgsConstructor
@Data
public class AssigmentCreateRequest extends AssigmentBaseRequest {

  private String name;
  private String description;
  private ScheduleModeEnum scheduleMode;
  private String cronExpression;
  private AssigmentConfig config;

  public AssignmentTaskEntity toAssignmentTask() {
    AssignmentTaskEntity assignment = new AssignmentTaskEntity();
    assignment.setId(null);
    assignment.setName(name);
    assignment.setDescription(description);
    assignment.setScheduleMode(scheduleMode);
    if (ScheduleModeEnum.SYSTEM_SCHEDULED == this.getScheduleMode()) {
      CronExprUtils.checkCronExpressionValid(this.getCronExpression(), CronExprUtils.MIN_INTERVAL_SECONDS);
      assignment.setCronExpression(this.getCronExpression());
    }

    return assignment;
  }

  public AssignmentConfigEntity toAssignmentConfig(Long assignmentId) {
    if (Objects.equals(config.getSourceConnectionId(), config.getTargetConnectionId())) {
      throw new DbswitchException(ResultCode.ERROR_INVALID_ASSIGNMENT_CONFIG, "源端与目标端不能相同");
    }

    AssignmentConfigEntity assignmentConfigEntity = toAssignmentConfig(assignmentId, config);
    assignmentConfigEntity.setFirstFlag(Boolean.TRUE);

    if (!assignmentConfigEntity.getExcludedFlag()
        && !CollectionUtils.isEmpty(assignmentConfigEntity.getSourceTables())) {
      for (String tableName : assignmentConfigEntity.getSourceTables()) {
        String targetTableName = PatterNameUtils.getFinalName(tableName,
            assignmentConfigEntity.getTableNameMap());
        if (StringUtils.isEmpty(targetTableName)) {
          throw new DbswitchException(
              ResultCode.ERROR_INVALID_ASSIGNMENT_CONFIG,
              "表名的映射关系配置有误，不允许将表[" + tableName + "]映射为空");
        }
      }
    }

    return assignmentConfigEntity;
  }

}
