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
import com.alinesno.infra.data.pipeline.common.response.Result;
import com.alinesno.infra.data.pipeline.common.response.ResultCode;
import com.alinesno.infra.data.pipeline.controller.converter.SystemUserDetailConverter;
import com.alinesno.infra.data.pipeline.dao.SystemUserDAO;
import com.alinesno.infra.data.pipeline.entity.SystemUserEntity;
import com.alinesno.infra.data.pipeline.model.response.SystemUserDetailResponse;
import com.alinesno.infra.data.pipeline.util.PasswordUtils;
import com.alinesno.infra.data.pipeline.util.ServletUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class SystemUserService {

  @Resource
  private SystemUserDAO systemUserDAO;

  public Result<SystemUserDetailResponse> getUserDetailById(Long id) {
    SystemUserEntity user = systemUserDAO.getById(id);
    if (Objects.isNull(user)) {
      return Result.failed(ResultCode.ERROR_RESOURCE_NOT_EXISTS, "id=" + id);
    }

    return Result.success(ConverterFactory.getConverter(SystemUserDetailConverter.class)
        .convert(user));
  }

  public Result<SystemUserDetailResponse> getUserDetailByUsername(String username) {
    SystemUserEntity user = findByUsername(username);
    if (Objects.isNull(user)) {
      return Result.failed(ResultCode.ERROR_RESOURCE_NOT_EXISTS, "username=" + username);
    }

    return Result.success(ConverterFactory.getConverter(SystemUserDetailConverter.class)
        .convert(user));
  }

  public Result changeOwnPassword(String oldPassword, String newPassword) {
    String username = ServletUtils.getHttpServletRequest().getAttribute("username").toString();
    SystemUserEntity systemUserEntity = findByUsername(username);
    if (Objects.isNull(systemUserEntity)) {
      return Result.failed(ResultCode.ERROR_USER_NOT_EXISTS, username);
    }

    String encryptOldPassword = PasswordUtils
        .encryptPassword(oldPassword, systemUserEntity.getSalt());
    if (!encryptOldPassword.equals(systemUserEntity.getPassword())) {
      return Result.failed(ResultCode.ERROR_USER_PASSWORD_WRONG, username);
    }

    String encryptNewPassword = PasswordUtils
        .encryptPassword(newPassword, systemUserEntity.getSalt());
    systemUserDAO.updateUserPassword(username, encryptNewPassword);

    return Result.success();
  }

  public SystemUserEntity findByUsername(String username) {
    return systemUserDAO.findByUsername(username);
  }

}
