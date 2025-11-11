// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.transfer.domain;

import com.alinesno.infra.data.pipeline.common.entity.CloseableDataSource;
import com.alinesno.infra.data.pipeline.core.basic.exchange.MemChannel;
import com.alinesno.infra.data.pipeline.core.basic.task.TaskParam;
import com.alinesno.infra.data.pipeline.transfer.config.DbswichPropertiesConfiguration;
import com.alinesno.infra.data.pipeline.core.schema.TableDescription;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
