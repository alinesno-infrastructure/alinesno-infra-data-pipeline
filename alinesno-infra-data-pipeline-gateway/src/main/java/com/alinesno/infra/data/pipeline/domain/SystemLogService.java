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

import com.alinesno.infra.data.pipeline.common.converter.ConverterFactory;
import com.alinesno.infra.data.pipeline.common.response.PageResult;
import com.alinesno.infra.data.pipeline.common.response.Result;
import com.alinesno.infra.data.pipeline.common.response.ResultCode;
import com.alinesno.infra.data.pipeline.controller.converter.SystemLogDetailConverter;
import com.alinesno.infra.data.pipeline.dao.SystemLogDAO;
import com.alinesno.infra.data.pipeline.entity.SystemLogEntity;
import com.alinesno.infra.data.pipeline.model.response.SystemLogDetailResponse;
import com.alinesno.infra.data.pipeline.type.LogTypeEnum;
import com.alinesno.infra.data.pipeline.util.PageUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Service
public class SystemLogService {

  @Resource
  private SystemLogDAO systemLogDAO;

  public PageResult<SystemLogDetailResponse> listAll(Integer type, Integer page, Integer size) {
    LogTypeEnum logType = LogTypeEnum.of(type);
    Supplier<List<SystemLogDetailResponse>> method = () -> {
      List<SystemLogEntity> systemLogEntities = systemLogDAO.listAll(logType);
      return ConverterFactory.getConverter(SystemLogDetailConverter.class).convert(
          systemLogEntities);
    };

    return PageUtils.getPage(method, page, size);
  }

  public Result<SystemLogDetailResponse> getDetailById(Long id) {
    SystemLogEntity systemLogEntity = systemLogDAO.getById(id);
    if (Objects.isNull(systemLogEntity)) {
      return Result.failed(ResultCode.ERROR_RESOURCE_NOT_EXISTS, "id=" + id);
    }

    return Result.success(ConverterFactory.getConverter(SystemLogDetailConverter.class)
        .convert(systemLogEntity));
  }

}
