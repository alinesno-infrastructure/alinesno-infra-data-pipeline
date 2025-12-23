// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.dao;

import com.alinesno.infra.data.pipeline.entity.SystemUserEntity;
import com.alinesno.infra.data.pipeline.mapper.SystemUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Objects;

@Repository
public class SystemUserDAO {

  @Resource
  private SystemUserMapper systemUserMapper;

  public SystemUserEntity getById(Long id) {
    return systemUserMapper.selectById(id);
  }

  public SystemUserEntity findByUsername(String username) {
    QueryWrapper<SystemUserEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(SystemUserEntity::getUsername, username);
    return systemUserMapper.selectOne(queryWrapper);
  }

  public void updateUserPassword(String username, String newPassword) {
    SystemUserEntity userEntity = findByUsername(username);
    if (Objects.nonNull(userEntity)) {
      userEntity.setPassword(newPassword);
      systemUserMapper.updateById(userEntity);
    }
  }

}
