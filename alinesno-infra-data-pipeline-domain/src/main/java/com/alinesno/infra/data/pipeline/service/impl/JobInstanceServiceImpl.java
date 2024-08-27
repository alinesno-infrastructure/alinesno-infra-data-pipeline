package com.alinesno.infra.data.pipeline.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.entity.JobInstanceEntity;
import com.alinesno.infra.data.pipeline.entity.TransformMonitorEntity;
import com.alinesno.infra.data.pipeline.enums.JobStatusEnums;
import com.alinesno.infra.data.pipeline.enums.TransTypeEnums;
import com.alinesno.infra.data.pipeline.mapper.JobInstanceMapper;
import com.alinesno.infra.data.pipeline.mapper.JobMapper;
import com.alinesno.infra.data.pipeline.mapper.TransMonitorMapper;
import com.alinesno.infra.data.pipeline.service.IJobInstanceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @version 1.0.0
 * @autor luoxiaodong
 */
@Slf4j
@Service
public class JobInstanceServiceImpl extends IBaseServiceImpl<JobInstanceEntity, JobInstanceMapper> implements IJobInstanceService {

    @Autowired
    private JobMapper jobMapper ;

    @Autowired
    private TransMonitorMapper transMonitorMapper ;

    @Override
    public void startMonitorJob(Long jobId, long jobInstanceId) {

        log.info("开始监控任务:{} , jobInstanceId:{}", jobId , jobInstanceId);
        long count = this.count(new LambdaQueryWrapper<JobInstanceEntity>().eq(JobInstanceEntity::getJobId, jobId)) ;

        JobEntity job = jobMapper.selectById(jobId) ;

        JobInstanceEntity e = new JobInstanceEntity() ;
        e.setJobId(jobId);

        e.setProjectId(job.getProjectId());

        e.setCountOrder((int)(count + 1L));
        e.setStartTime(System.currentTimeMillis()) ;
        e.setStatus(JobStatusEnums.PROCESSING.getStatus());
        e.setId(jobInstanceId);

        this.save(e);
    }

    @Override
    public void finishMonitorJob(Long jobId, long jobInstanceId, String status , String eMessage) {

        log.debug("结束监控任务:{} , jobInstanceId:{}", jobId , jobInstanceId);

        JobInstanceEntity jobInstance = this.getById(jobInstanceId) ;

        if (jobInstance != null) {
            jobInstance.setEndTime(System.currentTimeMillis());
            jobInstance.setStatus(status) ;
            jobInstance.setErrorMsg(eMessage);

            this.updateById(jobInstance);
        }

    }

    @Override
    public long getReaderCount(Long id) {
        LambdaQueryWrapper<TransformMonitorEntity> q = new LambdaQueryWrapper<TransformMonitorEntity>()
                .eq(TransformMonitorEntity::getJobInstanceId, id)
                .eq(TransformMonitorEntity::getType , TransTypeEnums.READER.getCode()) ;

        TransformMonitorEntity e = transMonitorMapper.selectOne(q);

        if (e != null && e.getProcessDataCount() != null) {
            return e.getProcessDataCount() ;
        }

        return 0;
    }

    @Override
    public long getWriterCount(Long id) {
        LambdaQueryWrapper<TransformMonitorEntity> q = new LambdaQueryWrapper<TransformMonitorEntity>()
                .eq(TransformMonitorEntity::getJobInstanceId, id)
                .eq(TransformMonitorEntity::getType , TransTypeEnums.WRITER.getCode()) ;

        TransformMonitorEntity e = transMonitorMapper.selectOne(q);

        if (e != null && e.getProcessDataCount() != null) {
            return e.getProcessDataCount() ;
        }

        return 0;
    }
}
