package com.alinesno.infra.data.pipeline.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.pipeline.entity.JobInstanceEntity;

/**
 * 【请填写功能名称】Service接口
 *
 * @version 1.0.0
 * @autor luoxiaodong
 */
public interface IJobInstanceService extends IBaseService<JobInstanceEntity> {

    /**
     * 启动监控任务
     * @param jobId
     */
    void startMonitorJob(Long jobId);

    /**
     * 结束监控任务
     * @param jobId
     * @param message
     * @param eMessage
     */
    void finishMonitorJob(Long jobId, String message, String eMessage);

}
