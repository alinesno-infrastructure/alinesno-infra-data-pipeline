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
import com.alinesno.infra.data.pipeline.entity.AssignmentConfigEntity;
import com.alinesno.infra.data.pipeline.entity.AssignmentTaskEntity;
import com.alinesno.infra.data.pipeline.type.ScheduleModeEnum;
import com.alinesno.infra.data.pipeline.util.CronExprUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@Data
public class AssigmentUpdateRequest extends AssigmentBaseRequest {

  private Long id;
  private String name;
  private String description;
  private ScheduleModeEnum scheduleMode;
  private String cronExpression;
  private AssigmentConfig config;

  public AssignmentTaskEntity toAssignmentTask() {
    AssignmentTaskEntity assignment = new AssignmentTaskEntity();
    assignment.setId(id);
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

    return assignmentConfigEntity;
  }
}
