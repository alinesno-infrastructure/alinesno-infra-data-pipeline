package com.alinesno.infra.data.pipeline.scheduler;

import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.entity.JobQueueEntity;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 分布式调度任务服务接口
 * 用于创建、停止、移除和列举任务
 */
public interface IDistSchedulerService {

    /**
     * 创建任务
     * @param taskInfoDto 任务信息DTO
     * @return 创建的任务的调度对象
     */
    Scheduled createTask(TaskInfoDto taskInfoDto);

    /**
     * 停止任务
     * @param taskId 任务ID
     */
    void stopTask(String taskId);

    /**
     * 移除任务
     * @param taskId 任务ID
     */
    void removeTask(String taskId);

    /**
     * 列举任务
     * @return 任务的调度对象列表
     */
    List<Scheduled> listTask();

    /**
     * 创建分布式链路任务
     * @param job
     */
    void createStepChainJob(JobQueueEntity job);

    /**
     * 执行定时任务
     * @param taskInfoDto
     * @param jobEntity
     * @param listTrans
     */
    void createCronJob(TaskInfoDto taskInfoDto, JobEntity jobEntity, List<TransEntity> listTrans) throws SQLException, IOException;

    /**
     * 执行定时任务
     * @param jobId
     */
    void createCronJob(Long jobId) throws SQLException, IOException;
}
