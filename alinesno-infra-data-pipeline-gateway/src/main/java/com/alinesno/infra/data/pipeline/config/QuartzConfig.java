package com.alinesno.infra.data.pipeline.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@Configuration("dbswitchQuartzConfig")
public class QuartzConfig {

    @Bean("quartzProperties")
    public Properties quartzProperties(DataSourceProperties dataSourceProperties) throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Properties prop = new Properties();

        /////////////////////////////////////
        // 基础配置
        /////////////////////////////////////
        prop.put("org.quartz.scheduler.instanceName", "DBSwitch-Quartz-Scheduler");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        prop.put("org.quartz.scheduler.rmi.export", "false");
        prop.put("org.quartz.scheduler.rmi.proxy", "false");
        prop.put("org.quartz.scheduler.wrapJobExecutionInUserTransaction", "false");

        /////////////////////////////////////
        // 调度器线程池配置
        /////////////////////////////////////
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "20");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");

        /////////////////////////////////////
        // Configure JobStore 作业存储配置
        /////////////////////////////////////
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        // 关键修复：指定数据源名称（必须配置，与Spring绑定的数据源关联）
        prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
        // 数据源配置（Quartz内部识别数据源的别名）
        prop.put("org.quartz.dataSource.quartzDataSource.driver", dataSourceProperties.getDriverClassName());
        prop.put("org.quartz.dataSource.quartzDataSource.URL", dataSourceProperties.getUrl());
        prop.put("org.quartz.dataSource.quartzDataSource.user", dataSourceProperties.getUsername());
        prop.put("org.quartz.dataSource.quartzDataSource.password", dataSourceProperties.getPassword());
        // 连接池配置（可选，优化连接管理）
        prop.put("org.quartz.dataSource.quartzDataSource.maxConnections", "10");

        // 持久化方式配置数据驱动
        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        prop.put("org.quartz.jobStore.useProperties", "true");
        prop.put("org.quartz.jobStore.isClustered", "true");
        prop.put("org.quartz.jobStore.misfireThreshold", "12000");
        prop.put("org.quartz.jobStore.tablePrefix", "DBSWITCH_");

        // PostgreSQL适配
        if (StringUtils.isNotBlank(dataSourceProperties.getUrl())
                && dataSourceProperties.getUrl().startsWith("jdbc:postgresql://")) {
            prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
        }

        propertiesFactoryBean.setProperties(prop);
        propertiesFactoryBean.afterPropertiesSet();

        return propertiesFactoryBean.getObject();
    }

    /**
     * SchedulerFactoryBean配置
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory, Properties quartzProperties) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties);
        // 可选：延迟启动（避免项目未完全初始化时Quartz启动失败）
        factory.setStartupDelay(1);
        return factory;
    }

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * 自定义JobFactory，支持Spring Bean自动注入
     */
    public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }
}