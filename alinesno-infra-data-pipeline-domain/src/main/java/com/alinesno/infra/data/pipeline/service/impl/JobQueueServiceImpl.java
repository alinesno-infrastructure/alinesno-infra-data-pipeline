package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.JobQueueEntity;
import com.alinesno.infra.data.pipeline.enums.JobStatusEnums;
import com.alinesno.infra.data.pipeline.mapper.IJobQueueMapper;
import com.alinesno.infra.data.pipeline.service.IJobQueueService;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobQueueServiceImpl extends IBaseServiceImpl<JobQueueEntity , IJobQueueMapper> implements IJobQueueService {

    private static final Logger log = LoggerFactory.getLogger(JobQueueServiceImpl.class) ;

    @Override
    public List<JobQueueEntity> queryQueue() {
        return new LambdaQueryChainWrapper<JobQueueEntity>(mapper)
                .ne(JobQueueEntity::getStatus , JobStatusEnums.FAILED.getStatus())
                .list();
    }
}
