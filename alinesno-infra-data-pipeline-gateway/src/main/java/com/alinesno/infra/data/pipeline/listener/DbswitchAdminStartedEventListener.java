package com.alinesno.infra.data.pipeline.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.alinesno.infra.data.pipeline.dao.JobLogbackDAO;
import com.alinesno.infra.data.pipeline.logback.LogbackAppenderRegister;
import com.alinesno.infra.data.pipeline.logback.LogbackEventContent;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DbswitchAdminStartedEventListener implements ApplicationListener<ApplicationReadyEvent> {

  private static final String[] LOGGER_CLASS_NAME = {
      "com.alinesno.infra.data.pipeline.domain.service.MigrationService",
      "com.alinesno.infra.data.pipeline.domain.service.DefaultReaderRobot",
      "com.alinesno.infra.data.pipeline.domain.handler.ReaderTaskThread",
      "com.alinesno.infra.data.pipeline.domain.service.DefaultWriterRobot",
      "com.alinesno.infra.data.pipeline.domain.handler.WriterTaskThread",
  };

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    LogbackAppenderRegister.addDatabaseAppender(Arrays.asList(LOGGER_CLASS_NAME), this::recordLogContent);
  }

  private void recordLogContent(LogbackEventContent log) {
    SpringUtil.getBean(JobLogbackDAO.class).insert(log.getIdentity(), log.getContent());
  }

}