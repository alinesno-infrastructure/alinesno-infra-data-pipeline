package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.JobMonitorEntity;
import com.alinesno.infra.data.pipeline.mapper.JobMonitorMapper;
import com.alinesno.infra.data.pipeline.service.IJobMonitorService;
import org.springframework.stereotype.Service;

/**
 * 数据抽取任务监控Service业务层处理
 *
 * @version 1.0.0
 * @autor luoxiaodong
 */
@Service
public class JobMonitorServiceImpl extends IBaseServiceImpl<JobMonitorEntity, JobMonitorMapper> implements IJobMonitorService {

}
