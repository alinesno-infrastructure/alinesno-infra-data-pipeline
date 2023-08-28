package com.alinesno.infra.data.pipeline.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.pipeline.entity.JobQueueEntity;

import java.util.List;

/**
 * 任务列表服务
 */
public interface IJobQueueService extends IBaseService<JobQueueEntity> {

    /**
     * 查询所有未处理完成任务列表
     */
    List<JobQueueEntity> queryQueue();

}
