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

import com.alinesno.infra.data.pipeline.common.annotation.LogOperate;
import com.alinesno.infra.data.pipeline.common.annotation.TokenCheck;
import com.alinesno.infra.data.pipeline.common.response.PageResult;
import com.alinesno.infra.data.pipeline.common.response.Result;
import com.alinesno.infra.data.pipeline.config.SwaggerConfig;
import com.alinesno.infra.data.pipeline.model.request.AssigmentCreateRequest;
import com.alinesno.infra.data.pipeline.model.request.AssigmentUpdateRequest;
import com.alinesno.infra.data.pipeline.model.request.AssignmentSearchRequest;
import com.alinesno.infra.data.pipeline.model.response.AssignmentDetailResponse;
import com.alinesno.infra.data.pipeline.model.response.AssignmentInfoResponse;
import com.alinesno.infra.data.pipeline.domain.AssignmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = {"任务管理接口"})
@RestController
@RequestMapping(value = SwaggerConfig.API_V1 + "/assignment")
public class AssignmentController {

  @Resource
  private AssignmentService assignmentService;

  @TokenCheck
  @LogOperate(name = "创建任务", description = "'创建任务的名称为：'+#request.name")
  @ApiOperation(value = "创建")
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<AssignmentInfoResponse> createAssignment(
      @RequestBody AssigmentCreateRequest request) {
    return Result.success(assignmentService.createAssignment(request));
  }

  @TokenCheck
  @LogOperate(name = "修改任务", description = "'修改任务的名称为：'+#request.name")
  @ApiOperation(value = "修改")
  @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result updateAssignment(@RequestBody AssigmentUpdateRequest request) {
    assignmentService.updateAssignment(request);
    return Result.success();
  }

  @TokenCheck
  @LogOperate(name = "删除任务", description = "'删除任务的ID为：'+#id")
  @ApiOperation(value = "删除")
  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result deleteAssignment(@PathVariable("id") Long id) {
    assignmentService.deleteAssignment(id);
    return Result.success();
  }

  @TokenCheck
  @ApiOperation(value = "任务列表")
  @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResult<AssignmentInfoResponse> listAssignment(@RequestBody AssignmentSearchRequest request) {
    return assignmentService.listAll(request);
  }

  @TokenCheck
  @ApiOperation(value = "任务详情")
  @GetMapping(value = "/detail/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<AssignmentDetailResponse> detailAssignment(
      @PathVariable("id") Long id) {
    return assignmentService.detailAssignment(id);
  }

  @TokenCheck
  @ApiOperation(value = "任务信息")
  @GetMapping(value = "/info/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<AssignmentInfoResponse> infoAssignment(
      @PathVariable("id") Long id) {
    return assignmentService.infoAssignment(id);
  }

  @TokenCheck
  @LogOperate(name = "发布任务", description = "'发布任务的ID为：'+#ids")
  @ApiOperation(value = "发布")
  @PostMapping(value = "/deploy", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result deployAssignments(
      @RequestParam(value = "ids") List<Long> ids) {
    assignmentService.deployAssignments(ids);
    return Result.success();
  }

  @TokenCheck
  @LogOperate(name = "手动执行任务", description = "'手动执行任务的ID为：'+#ids")
  @ApiOperation(value = "手动执行")
  @PostMapping(value = "/run", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result runAssignments(@RequestBody List<Long> ids) {
    assignmentService.runAssignments(ids);
    return Result.success();
  }

  @TokenCheck
  @LogOperate(name = "下线任务", description = "'下线任务的ID为：'+#ids")
  @ApiOperation(value = "下线")
  @PostMapping(value = "/retire", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result retireAssignments(@RequestParam(value = "ids") List<Long> ids) {
    assignmentService.retireAssignments(ids);
    return Result.success();
  }

  @TokenCheck
  @LogOperate(name = "导出任务", description = "'导出任务的ID为：'+#ids")
  @ApiOperation(value = "导出")
  @PostMapping(value = "/export")
  public void exportAssignments(@RequestParam(value = "ids") List<Long> ids, HttpServletResponse response) {
    assignmentService.exportAssignments(ids, response);
  }

}
