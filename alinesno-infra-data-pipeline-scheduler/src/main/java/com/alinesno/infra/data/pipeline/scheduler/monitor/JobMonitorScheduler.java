package com.alinesno.infra.data.pipeline.scheduler.monitor;

import com.alinesno.infra.data.pipeline.scheduler.IQuartzSchedulerService;
import com.alinesno.infra.data.pipeline.service.IJobQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 任务守护进程
 */
@Component
public class JobMonitorScheduler implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(JobMonitorScheduler.class) ;

    @Autowired
    private DataSource dataSource ;

    @Autowired
    private IJobQueueService jobQueueService ;

    @Autowired
    private IQuartzSchedulerService schedulerService ;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.debug("查询任务列队是否存在");

//        Schedule cron = Schedules.cron("*/3 * * * * ?");
//        RecurringTask<Void> cronTask = Tasks.recurring("cron-task", cron).execute((taskInstance, executionContext) -> {
//
//            log.info(Instant.now().getEpochSecond() + "s  -  Cron-schedule!");
//
//            List<JobQueueEntity> queueList = jobQueueService.queryQueue() ;
//
//            if(!queueList.isEmpty()){
//                for(JobQueueEntity job : queueList){
//                    schedulerService.createStepChainJob(job) ;
//                }
//            }
//
//        });
//
//        final Scheduler scheduler =
//                Scheduler.create(dataSource)
//                        .startTasks(cronTask)
//                        .pollingInterval(Duration.ofSeconds(1))
//                        .build();
//
//        scheduler.start();

    }
}