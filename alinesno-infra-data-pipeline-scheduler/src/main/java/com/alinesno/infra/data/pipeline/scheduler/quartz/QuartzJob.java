package com.alinesno.infra.data.pipeline.scheduler.quartz;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.common.core.context.SpringContext;
import com.alinesno.infra.data.pipeline.service.IJobInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

// 禁止并发执行
@Slf4j
@DisallowConcurrentExecution
public class QuartzJob extends QuartzJobBean {

    private IJobInstanceService jobInstanceService ;

   @Override
   protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        jobInstanceService = SpringContext.getBean(IJobInstanceService.class) ;

        long jobInstanceId = IdUtil.getSnowflakeNextId() ;

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap() ;
        String taskName = jobDataMap.getString("name");
        Long jobId = jobDataMap.getLong("jobId");

        log.info("---> Quartz job, time:{"+new Date()+"} ,name:{"+taskName+"}<----");

        // 输出运行结果
        jobInstanceService.startMonitorJob(jobId , jobInstanceId) ;
    }
}