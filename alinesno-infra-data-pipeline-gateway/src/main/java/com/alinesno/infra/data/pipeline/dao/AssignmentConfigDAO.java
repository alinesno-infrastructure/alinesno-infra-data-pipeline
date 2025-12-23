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

import com.alinesno.infra.data.pipeline.entity.AssignmentConfigEntity;
import com.alinesno.infra.data.pipeline.mapper.AssignmentConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class AssignmentConfigDAO {

  @Resource
  private AssignmentConfigMapper assignmentConfigMapper;

  public void insert(AssignmentConfigEntity assignmentConfigEntity) {
    assignmentConfigMapper.insert(assignmentConfigEntity);
  }

  public AssignmentConfigEntity getById(Long id) {
    return assignmentConfigMapper.selectById(id);
  }

  public AssignmentConfigEntity getByAssignmentTaskId(Long taskId) {
    QueryWrapper<AssignmentConfigEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(AssignmentConfigEntity::getAssignmentId, taskId);
    return assignmentConfigMapper.selectOne(queryWrapper);
  }

  public List<AssignmentConfigEntity> getByConnectionId(Long connId) {
    QueryWrapper<AssignmentConfigEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(AssignmentConfigEntity::getSourceConnectionId, connId)
        .or().eq(AssignmentConfigEntity::getTargetConnectionId, connId);
    return assignmentConfigMapper.selectList(queryWrapper);
  }

  public void updateSelective(AssignmentConfigEntity assignmentConfigEntity) {
    assignmentConfigMapper.updateById(assignmentConfigEntity);
  }

  public void deleteByAssignmentTaskId(Long taskId) {
    QueryWrapper<AssignmentConfigEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(AssignmentConfigEntity::getAssignmentId, taskId);
    assignmentConfigMapper.delete(queryWrapper);
  }

}
