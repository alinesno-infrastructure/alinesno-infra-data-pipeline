package com.alinesno.infra.data.pipeline.scheduler.quartz;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.common.core.context.SpringContext;
import com.alinesno.infra.data.pipeline.enums.JobStatusEnums;
import com.alinesno.infra.data.pipeline.scheduler.IQuartzSchedulerService;
import com.alinesno.infra.data.pipeline.service.IJobInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * 数据转换任务
 */
@Slf4j
@DisallowConcurrentExecution
public class DataTransferJob implements Job {

    private IJobInstanceService jobInstanceService ;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        jobInstanceService = SpringContext.getBean(IJobInstanceService.class) ;
        long jobInstanceId = IdUtil.getSnowflakeNextId() ;

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap() ;

        Long jobId = jobDataMap.getLong("jobId") ;
        log.debug("JobId = {}" , jobId);

        IQuartzSchedulerService distSchedulerService = SpringContext.getBean(IQuartzSchedulerService.class) ;

        String status = JobStatusEnums.COMPLETED.getStatus();
        String message = null ;
        try {

            jobInstanceService.startMonitorJob(jobId , jobInstanceId) ;
            distSchedulerService.createCronJob(jobId);

            jobInstanceService.finishMonitorJob(jobId , jobInstanceId , status , message) ;
        } catch (Exception e) {

            log.error("jobId = {} , error = {}", jobId, e.getMessage());
            status = JobStatusEnums.FAILED.getStatus();
            message = e.getMessage() ;

            jobInstanceService.finishMonitorJob(jobId , jobInstanceId , status , message) ;
            throw new RuntimeException(e);
        }


    }

}