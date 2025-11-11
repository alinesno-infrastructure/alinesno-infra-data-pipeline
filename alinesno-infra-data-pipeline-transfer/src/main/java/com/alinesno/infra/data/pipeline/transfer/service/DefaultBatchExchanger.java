// Copyright tang.  All rights reserved.
// https://gitee.com/inrgihc/dbswitch
//
// Use of this source code is governed by a BSD-style license
//
// Author: tang (inrgihc@126.com)
// Date : 2020/1/2
// Location: beijing , china
/////////////////////////////////////////////////////////////
package com.alinesno.infra.data.pipeline.transfer.service;

import com.alinesno.infra.data.pipeline.common.entity.PrintablePerfStat;
import com.alinesno.infra.data.pipeline.core.basic.exchange.AbstractBatchExchanger;
import com.alinesno.infra.data.pipeline.core.basic.robot.RobotReader;
import com.alinesno.infra.data.pipeline.core.basic.robot.RobotWriter;
import com.alinesno.infra.data.pipeline.transfer.domain.ComparePerfStat;
import com.alinesno.infra.data.pipeline.transfer.domain.ExceptPerfStat;
import com.alinesno.infra.data.pipeline.transfer.domain.ReaderPerfStat;
import com.alinesno.infra.data.pipeline.transfer.domain.ReaderTaskResult;
import com.alinesno.infra.data.pipeline.transfer.domain.WriterPerfStat;
import com.alinesno.infra.data.pipeline.transfer.domain.WriterTaskResult;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Optional;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * 连接读取和写入的交换器
 *
 * @author tang
 */
public class DefaultBatchExchanger extends AbstractBatchExchanger {

  private List<PrintablePerfStat> perfStats;

  public DefaultBatchExchanger(
      AsyncTaskExecutor readExecutor,
      AsyncTaskExecutor writeExecutor,
      int channelMaxSize,
      List<PrintablePerfStat> perfStats) {
    super(readExecutor, writeExecutor, channelMaxSize);
    this.perfStats = perfStats;
  }

  @Override
  protected Throwable collectPerfStats(RobotReader reader, RobotWriter writer) {
    Throwable throwable = null;
    Optional<ReaderTaskResult> readResult = reader.getWorkResult();
    Optional<WriterTaskResult> writeResult = writer.getWorkResult();
    if (readResult.isPresent() && writeResult.isPresent()) {
      ReaderTaskResult r = readResult.get();
      WriterTaskResult w = writeResult.get();
      long total = r.getSuccessCount() + r.getFailureCount();
      long failure = Sets.union(r.getExcept().keySet(), w.getExcept().keySet()).size();
      perfStats.add(new ReaderPerfStat(total, failure, r.getTotalBytes()));
      perfStats.add(new WriterPerfStat(w.getDuration()));
      perfStats.add(new ComparePerfStat(r.getPerf(), w.getPerf()));
      perfStats.add(new ExceptPerfStat(r.getExcept(), w.getExcept()));
      throwable = (null != w.getThrowable()) ? w.getThrowable() : r.getThrowable();
    } else {
      if (readResult.isPresent()) {
        ReaderTaskResult r = readResult.get();
        long total = r.getSuccessCount() + r.getFailureCount();
        perfStats.add(new ReaderPerfStat(total, r.getFailureCount(), r.getTotalBytes()));
        throwable = r.getThrowable();
      }
      if (writeResult.isPresent()) {
        WriterTaskResult w = writeResult.get();
        perfStats.add(new WriterPerfStat(w.getDuration()));
        throwable = w.getThrowable();
      }
    }
    return throwable;
  }
}
