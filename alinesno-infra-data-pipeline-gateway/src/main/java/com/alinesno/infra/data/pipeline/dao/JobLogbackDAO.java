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

import com.alinesno.infra.data.pipeline.entity.JobLogbackEntity;
import com.alinesno.infra.data.pipeline.mapper.JobLogbackMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class JobLogbackDAO {

  @Resource
  private JobLogbackMapper jobLogbackMapper;

  public void insert(String uuid, String content) {
    jobLogbackMapper.insert(JobLogbackEntity.builder().uuid(uuid).content(content).build());
  }

  public List<JobLogbackEntity> getTailByUuid(String uuid) {
    List<JobLogbackEntity> result = jobLogbackMapper.selectList(
        Wrappers.<JobLogbackEntity>lambdaQuery()
            .select(JobLogbackEntity::getId, JobLogbackEntity::getContent)
            .eq(JobLogbackEntity::getUuid, uuid)
            .orderByDesc(JobLogbackEntity::getId)
    );
    Collections.reverse(result);
    return result;
  }

  public List<JobLogbackEntity> getNextByUuid(String uuid, Long baseId) {
    return jobLogbackMapper.selectList(
        Wrappers.<JobLogbackEntity>lambdaQuery()
            .select(JobLogbackEntity::getId, JobLogbackEntity::getContent)
            .eq(JobLogbackEntity::getUuid, uuid)
            .gt(JobLogbackEntity::getId, baseId)
            .orderByAsc(JobLogbackEntity::getId)
    );
  }

  public void deleteOldest(Integer days) {
    if (Objects.nonNull(days)) {
      jobLogbackMapper.deleteByDays(days);
    }
  }

}
