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

import com.alinesno.infra.data.pipeline.entity.AssignmentTaskEntity;
import com.alinesno.infra.data.pipeline.mapper.AssignmentTaskMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class AssignmentTaskDAO {

  @Resource
  private AssignmentTaskMapper assignmentTaskMapper;

  public void insert(AssignmentTaskEntity assignment) {
    assignmentTaskMapper.insert(assignment);
  }

  public void updateById(AssignmentTaskEntity assignment) {
    assignmentTaskMapper.updateById(assignment);
  }

  public List<AssignmentTaskEntity> listAll(String searchText) {
    return assignmentTaskMapper.selectList(
        Wrappers.<AssignmentTaskEntity>lambdaQuery()
            .like(StringUtils.hasText(searchText), AssignmentTaskEntity::getName, searchText)
            .orderByDesc(AssignmentTaskEntity::getCreateTime)
    );
  }

  public AssignmentTaskEntity getById(Long id) {
    return assignmentTaskMapper.selectById(id);
  }

  public void deleteById(Long id) {
    assignmentTaskMapper.deleteById(id);
  }

  public int getTotalCount() {
    return assignmentTaskMapper.selectList(null).size();
  }

  public int getPublishedCount() {
    QueryWrapper<AssignmentTaskEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(AssignmentTaskEntity::getPublished, Boolean.TRUE);
    return assignmentTaskMapper.selectCount(queryWrapper).intValue();
  }

}
