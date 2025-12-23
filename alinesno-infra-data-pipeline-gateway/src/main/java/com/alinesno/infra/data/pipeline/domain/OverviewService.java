// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.domain;

import com.alinesno.infra.data.pipeline.dao.AssignmentJobDAO;
import com.alinesno.infra.data.pipeline.dao.AssignmentTaskDAO;
import com.alinesno.infra.data.pipeline.dao.DatabaseConnectionDAO;
import com.alinesno.infra.data.pipeline.model.ops.OpsTaskJobTrend;
import com.alinesno.infra.data.pipeline.model.response.OverviewStatisticsResponse;
import com.alinesno.infra.data.pipeline.model.response.OverviewStatisticsResponse.ConnectionStatistics;
import com.alinesno.infra.data.pipeline.model.response.OverviewStatisticsResponse.AssignmentTaskStatistics;
import com.alinesno.infra.data.pipeline.model.response.OverviewStatisticsResponse.AssignmentJobStatistics;
import com.alinesno.infra.data.pipeline.type.JobStatusEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OverviewService {

  @Resource
  private DatabaseConnectionDAO databaseConnectionDAO;

  @Resource
  private AssignmentTaskDAO assignmentTaskDAO;

  @Resource
  private AssignmentJobDAO assignmentJobDAO;

  public OverviewStatisticsResponse statistics() {
    OverviewStatisticsResponse response = new OverviewStatisticsResponse();

    ConnectionStatistics connectionStatistics = new ConnectionStatistics();
    connectionStatistics.setTotalCount(databaseConnectionDAO.getTotalCount());

    response.setConnectionStatistics(connectionStatistics);

    AssignmentTaskStatistics assignmentTaskStatistics = new AssignmentTaskStatistics();
    assignmentTaskStatistics.setTotalCount(assignmentTaskDAO.getTotalCount());
    assignmentTaskStatistics.setPublishedCount(assignmentTaskDAO.getPublishedCount());

    response.setAssignmentTaskStatistics(assignmentTaskStatistics);

    AssignmentJobStatistics assignmentJobStatistics = new AssignmentJobStatistics();
    assignmentJobStatistics.setTotalCount(assignmentJobDAO.getTotalCount());
    assignmentJobStatistics
        .setRunningCount(assignmentJobDAO.getCountByStatus(JobStatusEnum.RUNNING.getValue()));
    assignmentJobStatistics
        .setSuccessfulCount(assignmentJobDAO.getCountByStatus(JobStatusEnum.PASS.getValue()));
    assignmentJobStatistics.setFailedCount(assignmentJobDAO.getCountByStatus(JobStatusEnum.FAIL.getValue()));
    assignmentJobStatistics.setCancelCount(assignmentJobDAO.getCountByStatus(JobStatusEnum.CANCEL.getValue()));
    response.setAssignmentJobStatistics(assignmentJobStatistics);

    return response;
  }

  public List<OpsTaskJobTrend> trend(Integer days) {
    return assignmentJobDAO.queryTaskJobTrend(days);
  }

}
