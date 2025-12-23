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

import com.alinesno.infra.data.pipeline.entity.SystemLogEntity;
import com.alinesno.infra.data.pipeline.mapper.SystemLogMapper;
import com.alinesno.infra.data.pipeline.type.LogTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SystemLogDAO {

  @Resource
  private SystemLogMapper systemLogMapper;

  public void insert(SystemLogEntity systemLogEntity) {
    systemLogMapper.insert(systemLogEntity);
  }

  public List<SystemLogEntity> listAll(LogTypeEnum logType) {
    QueryWrapper<SystemLogEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(SystemLogEntity::getType, logType.getValue())
        .orderByDesc(SystemLogEntity::getCreateTime);
    return systemLogMapper.selectList(queryWrapper);
  }

  public SystemLogEntity getById(Long id) {
    return systemLogMapper.selectById(id);
  }

}
