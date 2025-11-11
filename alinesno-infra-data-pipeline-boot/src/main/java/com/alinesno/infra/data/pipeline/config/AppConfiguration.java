package com.alinesno.infra.data.pipeline.config;

import com.alinesno.infra.common.facade.enable.EnableActable;
import com.alinesno.infra.common.web.adapter.sso.enable.EnableInfraSsoApi;
import com.alinesno.infra.common.web.log.aspect.LogAspect;
import com.alinesno.infra.data.pipeline.initialize.IPipelineInitService;
import com.dtflys.forest.springboot.annotation.ForestScan;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 应用配置
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@EnableActable
@EnableInfraSsoApi
@EnableEncryptableProperties
@EnableAsync
@MapperScan("com.alinesno.infra.data.pipeline.mapper")
@ForestScan({
        "com.alinesno.infra.common.web.adapter.base.consumer" ,
        "com.alinesno.infra.base.platform.adapter"
})
@Configuration
public class AppConfiguration implements CommandLineRunner {

    @Autowired
    private IPipelineInitService pipelineInitService ;

    @Bean
    public LogAspect getLogAspect(){
        return new LogAspect() ;
    }

    @Override
    public void run(String... args) throws Exception {

        // pipelineInitService.initScheduler(); // 初始化定时任务

        pipelineInitService.initJobCatalog() ; // 初始化任务分类

        pipelineInitService.initDemoJob() ; // 初始化演示任务

        pipelineInitService.initReaderSource() ; // 初始化读取源
    }

}
