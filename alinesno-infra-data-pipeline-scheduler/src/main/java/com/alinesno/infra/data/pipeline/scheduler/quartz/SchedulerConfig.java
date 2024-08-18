package com.alinesno.infra.data.pipeline.scheduler.quartz;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class SchedulerConfig {

   @Autowired
    private DataSource dataSource;

    /**
     * 调度器
     *
     * @return
     * @throws Exception
     */
    @Bean
    public Scheduler scheduler() throws Exception {
        return schedulerFactoryBean().getScheduler();
    }

    /**
     * Scheduler工厂类
     *
     * @return
     * @throws IOException
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
       SchedulerFactoryBean factory = new SchedulerFactoryBean();
       factory.setSchedulerName("Cluster_Scheduler");
       factory.setDataSource(dataSource);
       factory.setApplicationContextSchedulerContextKey("applicationContext");
       factory.setAutoStartup(true);

       factory.setStartupDelay(30);// 延迟30s执行
       return factory;
    }

}