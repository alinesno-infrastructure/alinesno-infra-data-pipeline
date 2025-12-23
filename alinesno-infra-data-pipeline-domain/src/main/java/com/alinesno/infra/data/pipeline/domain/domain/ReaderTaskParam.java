// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.domain.domain;

import com.alinesno.infra.data.pipeline.common.entity.CloseableDataSource;
import com.alinesno.infra.data.pipeline.core.basic.exchange.MemChannel;
import com.alinesno.infra.data.pipeline.core.basic.task.TaskParam;
import com.alinesno.infra.data.pipeline.core.schema.TableDescription;
import com.alinesno.infra.data.pipeline.domain.config.DbswichPropertiesConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 读取任务线程的入参
 *
 * @author tang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReaderTaskParam implements TaskParam {

  private MemChannel memChannel;
  private TableDescription tableDescription;
  private DbswichPropertiesConfiguration configuration;
  private CloseableDataSource sourceDataSource;
  private CloseableDataSource targetDataSource;
  private Set<String> targetExistTables;
  private CountDownLatch countDownLatch;
}
