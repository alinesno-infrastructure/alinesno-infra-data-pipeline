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

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alinesno.infra.data.pipeline.transfer.config.DbswichPropertiesConfiguration;
import com.alinesno.infra.data.pipeline.transfer.entity.GlobalParamConfigProperties;
import com.alinesno.infra.data.pipeline.transfer.util.DataSourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.alinesno.infra.data.pipeline.common.entity.CloseableDataSource;
import com.alinesno.infra.data.pipeline.common.entity.LoggingRunnable;
import com.alinesno.infra.data.pipeline.common.entity.MdcKeyValue;
import com.alinesno.infra.data.pipeline.common.entity.PrintablePerfStat;
import com.alinesno.infra.data.pipeline.core.basic.exchange.AbstractBatchExchanger;
import com.alinesno.infra.data.pipeline.core.basic.robot.RobotReader;
import com.alinesno.infra.data.pipeline.core.basic.robot.RobotWriter;
import com.alinesno.infra.data.pipeline.transfer.util.MachineUtils;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/**
 * 数据迁移主逻辑类
 *
 * @author tang
 */
@Slf4j
@Service
public class MigrationService implements Runnable {

  /**
   * 性能统计记录表
   */
  private final List<PrintablePerfStat> perfStats = new ArrayList<>();

  /**
   * 配置参数
   */
  private final DbswichPropertiesConfiguration configuration;
  private final AsyncTaskExecutor readExecutor;
  private final AsyncTaskExecutor writeExecutor;

  private RobotReader robotReader;
  private RobotWriter robotWriter;

  /**
   * 任务执行实时记录MDC
   */
  private MdcKeyValue mdcKeyValue;

  /**
   * 构造函数
   *
   * @param properties 配置信息
   */
  public MigrationService(DbswichPropertiesConfiguration properties,
      AsyncTaskExecutor tableReadExecutor,
      AsyncTaskExecutor tableWriteExecutor) {
    this.configuration = Objects.requireNonNull(properties, "properties is null");
    this.readExecutor = Objects.requireNonNull(tableReadExecutor, "tableReadExecutor is null");
    this.writeExecutor = Objects.requireNonNull(tableWriteExecutor, "tableWriteExecutor is null");
  }

  public void setMdcKeyValue(MdcKeyValue mdcKeyValue) {
    this.mdcKeyValue = Objects.requireNonNull(mdcKeyValue, "mdcKeyValue is null");
  }

  /**
   * 中断执行中的任务
   */
  synchronized public void interrupt() {
    if (null != robotReader) {
      robotReader.interrupt();
    }
    if (null != robotWriter) {
      robotWriter.interrupt();
    }
  }

  /**
   * 执行入口
   */
  @Override
  public void run() {
    if (Objects.nonNull(mdcKeyValue)) {
      Runnable runnable = new LoggingRunnable(this::doRun, this.mdcKeyValue);
      runnable.run();
    } else {
      doRun();
    }
  }

  /**
   * 执行主逻辑
   */
  private void doRun() {
    log.info("dbswitch data service is started....");
    log.info("Task run environment information:\n{}", MachineUtils.getPrintInformation());
    //log.info("input configuration \n{}", JsonUtils.toJsonString(configuration));

    GlobalParamConfigProperties globalParam = configuration.getConfig();
    int maxQueueSize = globalParam.getChannelQueueSize();
    int writeThreadNum = globalParam.getWriteThreadNum();
    boolean concurrentWrite = DataSourceUtils.supportConcurrentWrite(configuration.getTarget());
    Throwable globalThrowable = null;
    StopWatch watch = new StopWatch();
    watch.start();

    AbstractBatchExchanger exchanger = new DefaultBatchExchanger(readExecutor, writeExecutor, maxQueueSize, perfStats);
    try (CloseableDataSource targetDataSource = DataSourceUtils.createTargetDataSource(configuration.getTarget())) {
      try (CloseableDataSource sourceDataSource = DataSourceUtils.createSourceDataSource(configuration.getSource())) {
        robotReader = new DefaultReaderRobot(mdcKeyValue, configuration, sourceDataSource, targetDataSource);
        robotWriter = new DefaultWriterRobot(mdcKeyValue, robotReader, writeThreadNum, concurrentWrite);
        boolean sourceSuccess = executeSqlScripts(sourceDataSource, configuration.getSource().getBeforeSqlScripts());
        try {
          boolean targetSuccess = executeSqlScripts(targetDataSource, configuration.getTarget().getBeforeSqlScripts());
          try {
            exchanger.exchange(robotReader, robotWriter);
          } finally {
            if (targetSuccess) {
              executeSqlScripts(targetDataSource, configuration.getTarget().getAfterSqlScripts());
            }
          }
        } finally {
          if (sourceSuccess) {
            executeSqlScripts(sourceDataSource, configuration.getSource().getAfterSqlScripts());
          }
        }
      }
    } catch (Throwable t) {
      globalThrowable = t;
      if (t instanceof RuntimeException) {
        throw (RuntimeException) t;
      }
      throw new RuntimeException(t);
    } finally {
      watch.stop();
      log.info("total ellipse = {} s", watch.getTotalTimeSeconds());

      if (!perfStats.isEmpty()) {
        StringBuilder sb = new StringBuilder();
        sb.append("=====================================\n");
        sb.append(String.format("total ellipse time:\t %f s\n", watch.getTotalTimeSeconds()));
        sb.append("-------------------------------------\n");
        perfStats.forEach(st -> sb.append(st.getPrintableString()));
        sb.append("=====================================\n");
        log.info("\n\n" + sb.toString());
      } else if (null != globalThrowable) {
        log.error("error:", globalThrowable);
      } else {
        log.error("!!!!!!!!!!Internal Error!!!!!!!");
      }

    }
  }

  private boolean executeSqlScripts(CloseableDataSource closeableDataSource, String sqlScripts) {
    if (StringUtils.isBlank(sqlScripts) || StringUtils.isBlank(sqlScripts.trim())) {
      return true;
    }
    List<String> sqlList = new ArrayList<>();
    ScriptUtils.splitSqlScript(null, sqlScripts,
        ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, ScriptUtils.DEFAULT_COMMENT_PREFIX,
        ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER, ScriptUtils.DEFAULT_BLOCK_COMMENT_END_DELIMITER,
        sqlList);
    if (!sqlList.isEmpty()) {
      try {
        try (Connection connection = closeableDataSource.getConnection();
            Statement statement = connection.createStatement()) {
          for (String sql : sqlList) {
            log.info("Execute sql : {}", sql);
            statement.execute(sql);
          }
        }
        return true;
      } catch (Throwable t) {
        log.error("Failed to execute sql script: {}", t.getMessage(), t);
      }
    }
    return false;
  }
}
