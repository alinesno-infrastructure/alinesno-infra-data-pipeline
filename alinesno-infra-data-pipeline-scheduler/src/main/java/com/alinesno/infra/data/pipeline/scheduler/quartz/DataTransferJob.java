package com.alinesno.infra.data.pipeline.scheduler.quartz;

import com.alinesno.infra.common.core.context.SpringContext;
import com.alinesno.infra.data.pipeline.scheduler.IQuartzSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 数据转换任务
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DataTransferJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap() ;

        Long jobId = jobDataMap.getLong("jobId") ;
        log.debug("JobId = {}" , jobId);

        IQuartzSchedulerService distSchedulerService = SpringContext.getBean(IQuartzSchedulerService.class) ;

        try {
            distSchedulerService.createCronJob(jobId);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}