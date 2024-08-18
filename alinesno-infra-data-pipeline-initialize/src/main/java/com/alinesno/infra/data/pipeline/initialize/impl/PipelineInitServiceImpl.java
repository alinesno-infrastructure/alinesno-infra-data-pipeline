package com.alinesno.infra.data.pipeline.initialize.impl;

import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import com.alinesno.infra.data.pipeline.initialize.IPipelineInitService;
import com.alinesno.infra.data.pipeline.initialize.builder.DataInitializer;
import com.alinesno.infra.data.pipeline.initialize.builder.DataSourceSamples;
import com.alinesno.infra.data.pipeline.service.IJobService;
import com.alinesno.infra.data.pipeline.service.IReaderSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 初始化服务实现类
 */
@Slf4j
@Service
public class PipelineInitServiceImpl implements IPipelineInitService {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private IJobService jobService ;

    @Autowired
    private IReaderSourceService readerSourceService ;

    @Override
    public void initScheduler() {

        try{
            jdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
            Resource classPathResource = new ClassPathResource("scheduler/init_scheduler.sql");

            EncodedResource encodedResource = new EncodedResource(classPathResource, "utf-8");
            ScriptUtils.executeSqlScript(dataSource.getConnection(), encodedResource);
        }catch (Exception e){
            log.error("初始化调度器异常:{}",e.getMessage());
        }
    }

    @Override
    public void initDemoJob() {
        List<JobEntity> jobs = DataInitializer.initializeJobs() ;
        jobService.saveOrUpdateBatch(jobs) ;
    }

    @Override
    public void initReaderSource() {
        List<ReaderSourceEntity> sampleDataSources = DataSourceSamples.createSampleDataSources();

        AtomicLong id = new AtomicLong(1L);
        sampleDataSources.forEach(e -> {
            e.setId(id.getAndIncrement());
            if(id.get()%3 == 0){
                e.setSourceType("source");
            }else{
                e.setSourceType("sink");
            }
        }) ;

        readerSourceService.saveOrUpdateBatch(sampleDataSources) ;
        log.info("初始化读取源示例成功");
    }
}
