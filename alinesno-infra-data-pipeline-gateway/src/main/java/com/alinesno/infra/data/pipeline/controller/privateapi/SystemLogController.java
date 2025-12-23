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
import com.alinesno.infra.data.pipeline.model.response.SystemLogDetailResponse;
import com.alinesno.infra.data.pipeline.domain.SystemLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = {"审计日志接口"})
@RestController
@RequestMapping(value = SwaggerConfig.API_V1 + "/syslog")
public class SystemLogController {

  @Resource
  private SystemLogService systemLogService;

  @TokenCheck
  @ApiOperation(value = "日志列表")
  @GetMapping(value = "/list/{type}/{page}/{size}", produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResult<SystemLogDetailResponse> listAll(
      @PathVariable("type") Integer type,
      @PathVariable("page") Integer page,
      @PathVariable("size") Integer size) {
    return systemLogService.listAll(type, page, size);
  }

  @TokenCheck
  @ApiOperation(value = "日志详情")
  @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Result<SystemLogDetailResponse> getDetail(
      @PathVariable("id") Long id) {
    return systemLogService.getDetailById(id);
  }

}
