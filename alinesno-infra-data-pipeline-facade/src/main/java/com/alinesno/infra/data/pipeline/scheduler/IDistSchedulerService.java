package com.alinesno.infra.data.pipeline.scheduler;

import com.alinesno.infra.data.pipeline.entity.JobEntity;
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
