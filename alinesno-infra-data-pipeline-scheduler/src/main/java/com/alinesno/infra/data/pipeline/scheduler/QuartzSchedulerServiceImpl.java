package com.alinesno.infra.data.pipeline.scheduler;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.core.context.SpringContext;
import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.datasource.IDataSourceReader;
import com.alinesno.infra.data.pipeline.datasource.IDataSourceWriter;
import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.enums.JobStatusEnums;
import com.alinesno.infra.data.pipeline.enums.TransTypeEnums;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;
import com.alinesno.infra.data.pipeline.scheduler.event.AlarmEvent;
import com.alinesno.infra.data.pipeline.scheduler.event.AlarmEventPublisher;
import com.alinesno.infra.data.pipeline.scheduler.listener.PipelineJobListener;
import com.alinesno.infra.data.pipeline.scheduler.quartz.DataTransferJob;
import com.alinesno.infra.data.pipeline.service.IJobInstanceService;
import com.alinesno.infra.data.pipeline.service.IJobService;
import com.alinesno.infra.data.pipeline.service.ITransService;
import com.alinesno.infra.data.pipeline.transfer.IDataSourcePlugins;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

/**
 * 分布式调度任务服务实现类
 */
@Slf4j
@Service
public class QuartzSchedulerServiceImpl implements IQuartzSchedulerService {

    @Autowired
    private Scheduler scheduler ;

    @Autowired
    private ITransService transService;

    @Autowired
    private IDataSourcePlugins dataSourcePlugins;

    @Autowired
    private IJobInstanceService jobInstanceService ;

    @Autowired
    private IJobService jobService;

    @Value("${alinesno.data.pipeline.workspacePath}")
    private String workspacePath;

    @Autowired
    private AlarmEventPublisher alarmEventPublisher ;

    public QuartzSchedulerServiceImpl() {
        // 构造函数
    }

    @Override
    public void createCronJob(TaskInfoDto taskInfoDto, JobEntity jobEntity, List<TransEntity> listTrans) throws SQLException, IOException {

        IDataSourceReader dataSourceReader = (IDataSourceReader) SpringContext.getBean(taskInfoDto.getReader().getType() + PipeConstants.READER_SUFFIX);
        IDataSourceWriter dataSourceWriter = (IDataSourceWriter) SpringContext.getBean(taskInfoDto.getWriter().getType() + PipeConstants.WRITER_SUFFIX);

        // 使用Comparator接口创建一个比较器，按照order_step字段的值从小到大排序
        Comparator<TransEntity> comparator = Comparator.comparingInt(TransEntity::getOrderStep);

        // 使用Collections工具类的sort方法对列表进行排序
        listTrans.sort(comparator);

        String jobWorkspace = workspacePath + File.separator + jobEntity.getId();
        File jobWorkspaceDir = new File(jobWorkspace);
        if (!jobWorkspaceDir.exists()) {
            FileUtils.forceMkdir(jobWorkspaceDir);
        }

        File sourceFile = null;

        int step = 1 ;
        long readDataCount = 0 ;
        for (TransEntity trans : listTrans) {

            log.debug("--->>>>>>>>>>> step = {} , trans = {}", step ++ , JSONObject.toJSONString(trans));

//            if(JobStatusEnums.COMPLETED.getStatus().equals(trans.getStatus())){
//               continue;
//            }

            // 更新任务状态
            trans.setStatus(JobStatusEnums.PROCESSING.getStatus());
            transService.update(trans);

            try {

                if (trans.getType().equals(TransTypeEnums.READER.getCode())) {  // 读取任务
                    // 读取数据
                    sourceFile = dataSourceReader.readData(taskInfoDto, jobWorkspace , trans);
                    readDataCount = trans.getProcessDataCount();

                } else if (trans.getType().equals(TransTypeEnums.PLUGIN.getCode())) {  // 插件处理

                    trans.setTotalDataCount(readDataCount);
                    // 数据插件处理
                    log.debug("插件数据处理:{}", trans.toString());

                    // File filterFile = dataSourcePlugins.transformData(taskInfoDto , sourceFile) ;
                } else if (trans.getType().equals(TransTypeEnums.WRITER.getCode())) {  // 写入处理

                    trans.setTotalDataCount(readDataCount);
                    // 写入数据
                    dataSourceWriter.writerData(taskInfoDto, sourceFile , trans);
                }

                // 更新任务状态
                trans.setStatus(JobStatusEnums.COMPLETED.getStatus());
                transService.update(trans);

            } catch (Exception e) {
                trans.setStatus(JobStatusEnums.FAILED.getStatus());
                transService.update(trans);

                AlarmEvent alarmEvent = new AlarmEvent(trans.getId()) ;
                alarmEvent.setStep(step);
                alarmEvent.setTransName(trans.getName());

                alarmEventPublisher.publishEvent(alarmEvent);

                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void createCronJob(Long jobId) throws SQLException, IOException {

        JobEntity jobEntity = jobService.getById(jobId);
        TaskInfoDto taskInfoDto = JSONObject.parseObject(jobEntity.getJobContext(), TaskInfoDto.class);

        List<TransEntity> transList = transService.list(new LambdaQueryWrapper<TransEntity>().eq(TransEntity::getJobId, jobId));

        // 使用Comparator接口创建一个比较器，按照order_step字段的值从小到大排序
        Comparator<TransEntity> comparator = Comparator.comparingInt(TransEntity::getOrderStep);

        // 使用Collections工具类的sort方法对列表进行排序
        transList.sort(comparator);

        StopWatch stopWatch = new StopWatch("func2");
        stopWatch.start("phase1");
        log.info("开始进行数据查询.");

        this.createCronJob(taskInfoDto, jobEntity, transList);

        stopWatch.stop();
        log.info("转换成流式查询时间:{}", SystemUtils.formatMilliseconds(stopWatch.getLastTaskTimeMillis()));
    }

    @SneakyThrows
    @Override
    public void addJob(String jobId, String cron) {
        Assert.hasLength(jobId , "任务标识为空");
        Assert.hasLength(cron , "触发构建为空");

//        Scheduler scheduler = SpringContext.getBean(Scheduler.class);
//        TriggerKey triggerKey = TriggerKey.triggerKey(jobId , PipeConstants.TRIGGER_GROUP_NAME);
//        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        JobDetail jobDetail = JobBuilder.newJob(DataTransferJob.class)
                .usingJobData("jobId", jobId)
                .withIdentity(jobId , PipeConstants.JOB_GROUP_NAME)
                .build();//执行

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .usingJobData("jobId", jobId)
                .withIdentity(jobId , PipeConstants.TRIGGER_GROUP_NAME)
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();

        scheduler.scheduleJob(jobDetail, trigger);

        // 添加任务监听器
        JobListener jobListener = new PipelineJobListener(jobDetail.getKey().getName() , Long.valueOf(jobId) , jobInstanceService);
        scheduler.getListenerManager().addJobListener(jobListener);

        log.debug("Quartz 创建了job:{}" , jobDetail.getKey());
    }

}
