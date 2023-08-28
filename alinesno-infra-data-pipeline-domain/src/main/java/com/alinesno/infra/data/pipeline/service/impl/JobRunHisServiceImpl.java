package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.JobRunHisEntity;
import com.alinesno.infra.data.pipeline.mapper.JobRunHisMapper;
import com.alinesno.infra.data.pipeline.service.IJobRunHisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 作业执行历史记录Service业务层处理
 *
 * @version 1.0.0
 * @autor luoxiaodong
 */
@Service
public class JobRunHisServiceImpl extends IBaseServiceImpl<JobRunHisEntity, JobRunHisMapper> implements IJobRunHisService {
    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(JobRunHisServiceImpl.class);
}
