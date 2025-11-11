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

import com.alinesno.infra.data.pipeline.core.basic.exchange.MemChannel;
import com.alinesno.infra.data.pipeline.core.basic.robot.RobotReader;
import com.alinesno.infra.data.pipeline.core.basic.task.TaskParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 写入任务线程的执行结果
 *
 * @author tang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WriterTaskParam implements TaskParam {

  private MemChannel memChannel;
  private RobotReader robotReader;
  private boolean concurrentWrite;
}
