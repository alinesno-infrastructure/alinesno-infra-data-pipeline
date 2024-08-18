package com.alinesno.infra.data.pipeline.scheduler.controller;

import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.scheduler.quartz.DataTransferJob;
import com.alinesno.infra.data.pipeline.scheduler.quartz.QuartzJobsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * QuartzController
 */
@Slf4j
@RestController
@RequestMapping("/v1/api/data/quartz/")
public class QuartzController {

    @Autowired
    private Scheduler scheduler ;

    @PostMapping("addJob")
    public AjaxResult addJob(String jobId , String cron) throws SchedulerException {

        Assert.hasLength(jobId , "任务标识为空");
        Assert.hasLength(cron , "触发构建为空");

        TriggerKey triggerKey = TriggerKey.triggerKey(jobId , PipeConstants.TRIGGER_GROUP_NAME);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        if(trigger != null){
            return AjaxResult.error(jobId, "定时任务已存在.") ;
        }

        JobDetail jobDetail = JobBuilder.newJob(DataTransferJob.class)
                .usingJobData("jobId", jobId)
                .withIdentity(jobId , PipeConstants.JOB_GROUP_NAME)
                .build();//执行

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        trigger = TriggerBuilder.newTrigger()
                .usingJobData("jobId", jobId)
                .withIdentity(jobId , PipeConstants.TRIGGER_GROUP_NAME)
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        log.debug("Quartz 创建了job:{}" , jobDetail.getKey());

        return AjaxResult.success();
    }

    /**
     * 暂停触发器
     * @param jobId
     * @return
     */
    @PostMapping("pauseTrigger")
    public AjaxResult pauseTrigger(String jobId) throws SchedulerException {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobId , PipeConstants.TRIGGER_GROUP_NAME));
        return AjaxResult.success();
    }

    /**
     * 运行一次
     * @param jobId
     * @return
     */
    @PostMapping("runOneTime")
    public AjaxResult runOneTime(String jobId) throws SchedulerException {

        JobKey jobKey = JobKey.jobKey(jobId,PipeConstants.JOB_GROUP_NAME);
        scheduler.triggerJob(jobKey);

        return AjaxResult.success() ;
    }

    /**
     * 启动触发器
     * @param jobId
     * @return
     */
    @PostMapping("startJob")
    public AjaxResult startJob(String jobId) throws SchedulerException {
        scheduler.resumeTrigger(TriggerKey.triggerKey(jobId , PipeConstants.TRIGGER_GROUP_NAME));//恢复Trigger
        return AjaxResult.success();
    }

    /**
     * 移除触发器
     * @param jobId
     * @return
     */
    @PostMapping("unscheduleJob")
    public AjaxResult unscheduleJob(String jobId) throws SchedulerException {
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobId , PipeConstants.TRIGGER_GROUP_NAME));//移除触发器
        return AjaxResult.success();
    }

    /**
     * 任务的恢复
     * @param jobId
     * @return
     */
    @PostMapping("resumeTrigger")
    public AjaxResult resumeTrigger(String jobId) throws SchedulerException {
        scheduler.resumeTrigger(TriggerKey.triggerKey(jobId , PipeConstants.TRIGGER_GROUP_NAME)) ;
        return AjaxResult.success();
    }

    /**
     * 删除任务
     * @param jobId
     * @return
     */
    @PostMapping("deleteJob")
    public AjaxResult deleteJob(String jobId) throws SchedulerException {

        scheduler.pauseTrigger(TriggerKey.triggerKey(jobId , PipeConstants.TRIGGER_GROUP_NAME));//暂停触发器
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobId , PipeConstants.TRIGGER_GROUP_NAME));//移除触发器
        scheduler.deleteJob(JobKey.jobKey(jobId , PipeConstants.JOB_GROUP_NAME));//删除Job

        return AjaxResult.success();
    }

    /**
     * 删除所有任务
     * @return
     */
    @GetMapping("deleteAllJob")
    public AjaxResult deleteAllJob(String groupId) throws SchedulerException {

        GroupMatcher<JobKey> matcher = GroupMatcher .anyJobGroup();

        if(StringUtils.isNoneBlank(groupId)){
           matcher = GroupMatcher.groupEquals(groupId)  ;
        }

        List<QuartzJobsVO> jobList = new ArrayList<>();

        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        for (JobKey jobKey : jobKeys) {

            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                scheduler.pauseTrigger(trigger.getKey()) ;
                scheduler.unscheduleJob(trigger.getKey()) ;
            }

            scheduler.deleteJob(jobKey);//删除Job

        }


        return AjaxResult.success();
    }

    /**
     * 列出定时任务
     * @return
     */
    @GetMapping("/getAllJobs")
    public List<QuartzJobsVO> getAllJob(String groupId){
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();

        if(StringUtils.isNoneBlank(groupId)){
            matcher = GroupMatcher.groupEquals(groupId)  ;
        }

        Set<JobKey> jobKeys = null;
        List<QuartzJobsVO> jobList = new ArrayList<>();
        try {
            jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

                for (Trigger trigger : triggers) {

                    QuartzJobsVO job = new QuartzJobsVO();
                    job.setJobDetailName(jobKey.getName());
                    job.setGroupName(jobKey.getGroup());
                    job.setJobCronExpression("触发器:" + trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    job.setStatus(triggerState.name());

                    if (trigger instanceof CronTrigger cronTrigger) {
                        String cronExpression = cronTrigger.getCronExpression();
                        job.setJobCronExpression(cronExpression);
                    }
                    jobList.add(job);
                }
            }

        } catch (SchedulerException e) {
            log.error("任务异常:{}" , e.getMessage());
        }
        return jobList;

    }

    /**
     * 更新定时任务
     * @param jobId
     * @param cron
     * @return
     */
    @PostMapping("updateJob")
    public AjaxResult updateJob(String jobId , String cron) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(jobId , PipeConstants.TRIGGER_GROUP_NAME);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .usingJobData("jobId", jobId)
                .withIdentity(jobId , PipeConstants.TRIGGER_GROUP_NAME)
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();

        //按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
        return AjaxResult.success();
    }
}
