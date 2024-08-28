package com.alinesno.infra.data.pipeline.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.pipeline.api.JobConfig;
import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.service.IJobCatalogService;
import com.alinesno.infra.data.pipeline.service.IJobService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 处理与JobEntity相关的请求的Controller。
 * 继承自BaseController类并实现IJobService接口。
 *
 * @version 1.0.0
 * @author  luoxiaodong
 */
@Slf4j
@Api(tags = "Job")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/pipeline/job")
public class JobController extends BaseController<JobEntity, IJobService> {

    @Autowired
    private IJobService service;

    @Autowired
    private IJobCatalogService catalogService ;

    @Autowired
    private Scheduler scheduler ;

    /**
     * 获取JobEntity的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param page    DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        return this.toPage(model, this.getFeign(), page);
    }

    /**
     * 运行数据抽取服务
     * @param jobConfig
     * @return
     */
    @PostMapping("commitJob")
    public AjaxResult commitJob(@RequestBody JSONObject jobConfig) throws SQLException, IOException {

        log.debug("taskInfo = {}" , jobConfig.toJSONString());
        JobConfig config = JSONObject.toJavaObject(jobConfig , JobConfig.class) ;
        log.debug("taskInfoObj = {}", config.toString());

        return AjaxResult.success("任务保存成功.");
    }

    /**
     * 运行一次
     * @param jobId
     * @return
     */
    @PostMapping("runOneTime")
    public AjaxResult runOneTime(String jobId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobId, PipeConstants.JOB_GROUP_NAME);
        scheduler.triggerJob(jobKey);

        return AjaxResult.success() ;
    }

    @GetMapping("/catalogTreeSelect")
    public AjaxResult catalogTreeSelect(){
        return AjaxResult.success("success" , catalogService.selectCatalogTreeList()) ;
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

    @Override
    public IJobService getFeign() {
        return this.service;
    }
}
