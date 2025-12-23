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

import com.alinesno.infra.data.pipeline.common.response.Result;
import com.alinesno.infra.data.pipeline.common.response.ResultCode;
import com.alinesno.infra.data.pipeline.entity.SystemUserEntity;
import com.alinesno.infra.data.pipeline.model.response.AccessTokenResponse;
import com.alinesno.infra.data.pipeline.util.CacheUtils;
import com.alinesno.infra.data.pipeline.util.PasswordUtils;
import com.alinesno.infra.data.pipeline.util.ServletUtils;
import com.alinesno.infra.data.pipeline.util.TokenUtils;
import com.mchange.v2.lang.StringUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Service
public class AuthenticationService {

  @Resource
  private SystemUserService systemUserService;

  public Result<AccessTokenResponse> login(String username, String password) {
    SystemUserEntity user = systemUserService.findByUsername(username);
    if (Objects.isNull(user)) {
      return Result.failed(ResultCode.ERROR_USER_NOT_EXISTS, username);
    }

    String encryptPassword = PasswordUtils.encryptPassword(password, user.getSalt());
    if (!encryptPassword.equals(user.getPassword())) {
      return Result.failed(ResultCode.ERROR_USER_PASSWORD_WRONG, username);
    }

    String token = TokenUtils.generateValue();
    CacheUtils.put(token, user);
    AccessTokenResponse accessTokenWrapper = new AccessTokenResponse(user.getRealName(), token,
        CacheUtils.CACHE_DURATION_SECONDS);
    return Result.success(accessTokenWrapper);
  }

  public Result logout() {
    String token = TokenUtils.getRequestToken(ServletUtils.getHttpServletRequest());
    if (StringUtils.nonEmptyString(token)) {
      CacheUtils.remove(token);
    }

    log.info("logout with token:{}", token);
    return Result.success();
  }

}
