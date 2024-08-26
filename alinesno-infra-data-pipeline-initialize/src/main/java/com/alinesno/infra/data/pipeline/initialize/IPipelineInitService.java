package com.alinesno.infra.data.pipeline.initialize;

/**
 * 初始化服务接口
 */
public interface IPipelineInitService {

    /**
     * 初始化调度器
     */
    void initScheduler();

    /**
     * 初始化演示任务
     */
    void initDemoJob();

    /**
     * 初始化读取源
     */
    void initReaderSource();

    /**
     * 初始化任务分类
     */
    void initJobCatalog();

}
