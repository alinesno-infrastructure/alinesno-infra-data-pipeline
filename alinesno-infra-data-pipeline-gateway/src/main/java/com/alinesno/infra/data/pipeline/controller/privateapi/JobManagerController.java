// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.controller.privateapi;

import com.alinesno.infra.data.pipeline.common.annotation.TokenCheck;
import com.alinesno.infra.data.pipeline.common.response.PageResult;
import com.alinesno.infra.data.pipeline.common.response.Result;
import com.alinesno.infra.data.pipeline.config.SwaggerConfig;
import com.alinesno.infra.data.pipeline.model.response.TaskJobDetailResponse;
import com.alinesno.infra.data.pipeline.model.response.TaskJobLogbackResponse;
import com.alinesno.infra.data.pipeline.domain.JobLogbackService;
import com.alinesno.infra.data.pipeline.domain.JobManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = {"作业管理接口"})
@RestController
@RequestMapping(value = SwaggerConfig.API_V1 + "/ops")
public class JobManagerController {

  @Resource
  private JobManagerService opsManagerService;
  @Resource
  private JobLogbackService jobLogbackService;

  @TokenCheck
  @ApiOperation(value = "根据任务ID查询作业执行记录")
  @GetMapping(value = "/jobs/list/{page}/{size}", produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResult<TaskJobDetailResponse> listJobs(@RequestParam("id") Long id,
                                                    @PathVariable("page") Integer page,
                                                    @PathVariable("size") Integer size) {
    return opsManagerService.listJobs(id, page, size);
  }

  @TokenCheck
  @ApiOperation(value = "根据作业的ID查询执行记录")
  @GetMapping(value = "/job/detail", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<TaskJobDetailResponse> detailJob(@RequestParam("id") Long id) {
    return opsManagerService.detailJob(id);
  }

  @TokenCheck
  @ApiOperation(value = "根据作业的ID取消JOB作业")
  @GetMapping(value = "/job/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<Boolean> cancelJob(@RequestParam("id") Long id) {
    return opsManagerService.cancelJob(id);
  }

  @TokenCheck
  @ApiOperation(value = "根据作业的ID查询最后N条日志")
  @GetMapping(value = "/job/logs/tail", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<TaskJobLogbackResponse> tailLogs(@RequestParam("id") Long id, @RequestParam("size") Integer size) {
    return jobLogbackService.tailLog(id, size);
  }

  @TokenCheck
  @ApiOperation(value = "根据作业的ID查询后续日志")
  @GetMapping(value = "/job/logs/next", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<TaskJobLogbackResponse> nextLogs(@RequestParam("id") Long id, @RequestParam("baseId") Long baseId) {
    return jobLogbackService.nextLog(id, baseId);
  }

}
