package com.alinesno.infra.data.pipeline.initialize.impl;

import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.initialize.IPipelineInitService;
import com.alinesno.infra.data.pipeline.initialize.builder.DataInitializer;
import com.alinesno.infra.data.pipeline.service.IJobService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * 初始化服务实现类
 */
@Slf4j
@Service
public class PipelineInitServiceImpl implements IPipelineInitService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private IJobService jobService ;

    @SneakyThrows
    @Override
    public void initScheduler() {
        Resource classPathResource = new ClassPathResource("scheduler/init_scheduler.sql");

        EncodedResource encodedResource = new EncodedResource(classPathResource, "utf-8");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), encodedResource);

        log.info("初始化定时任务成功");
    }

    @Override
    public void initDemoJob() {
        List<JobEntity> jobs = DataInitializer.initializeJobs() ;
        jobService.saveOrUpdateBatch(jobs) ;
    }
}
