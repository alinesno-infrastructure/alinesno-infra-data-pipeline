package com.alinesno.infra.data.pipeline.initialize.impl;

import cn.hutool.core.util.IdUtil;
import com.alinesno.infra.data.pipeline.entity.JobCatalogEntity;
import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import com.alinesno.infra.data.pipeline.initialize.IPipelineInitService;
import com.alinesno.infra.data.pipeline.initialize.builder.DataInitializer;
import com.alinesno.infra.data.pipeline.initialize.builder.DataSourceSamples;
import com.alinesno.infra.data.pipeline.service.IJobCatalogService;
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
import java.util.ArrayList;
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
    private IJobCatalogService promptCatalogService ;

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
                e.setOperationType("source");
            }else{
                e.setOperationType("sink");
            }
        }) ;

        readerSourceService.saveOrUpdateBatch(sampleDataSources) ;
        log.info("初始化读取源示例成功");
    }

    @Override
    public void initJobCatalog() {

        long count = promptCatalogService.count() ;
        if(count != 0){
            return ;
        }

        List<JobCatalogEntity> rootEntities = new ArrayList<>();

        JobCatalogEntity rootEntity = new JobCatalogEntity();
        rootEntity.setName("数据分类");
        rootEntity.setDescription("所有系统的数据分类");
        rootEntity.setIcon("");
        rootEntity.setOrderNum(1);
        rootEntity.setParentId(0L);
        rootEntity.setChildren(new ArrayList<>());

        // 电商管理系统
        JobCatalogEntity ecomManagement = createEntity("电商管理系统", "电商管理系统", rootEntity);
        JobCatalogEntity ecomSalesAnalysis = createEntity("销售数据", "电商系统的销售数据", ecomManagement);
        JobCatalogEntity ecomCustomerBehavior = createEntity("顾客行为", "电商系统的顾客行为", ecomManagement);
        rootEntities.add(ecomManagement);
        rootEntities.add(ecomSalesAnalysis);
        rootEntities.add(ecomCustomerBehavior);

        // 财务管理系统
        JobCatalogEntity financialManagement = createEntity("财务管理系统", "财务管理系统", rootEntity);
        JobCatalogEntity financialReporting = createEntity("财务报表", "财务系统的报表", financialManagement);
        JobCatalogEntity financialForecasting = createEntity("财务预测", "财务系统的预测", financialManagement);
        rootEntities.add(financialManagement);
        rootEntities.add(financialReporting);
        rootEntities.add(financialForecasting);

        // 人力资源管理系统
        JobCatalogEntity hrManagement = createEntity("人力资源管理系统", "人力资源管理系统", rootEntity);
        JobCatalogEntity hrPerformanceAnalysis = createEntity("绩效数据", "人力资源系统的绩效数据", hrManagement);
        JobCatalogEntity hrRecruitmentAnalysis = createEntity("招聘数据", "人力资源系统的招聘数据", hrManagement);
        rootEntities.add(hrManagement);
        rootEntities.add(hrPerformanceAnalysis);
        rootEntities.add(hrRecruitmentAnalysis);

        // 客户关系管理系统
        JobCatalogEntity crmSystem = createEntity("客户关系管理系统", "客户关系管理系统", rootEntity);
        JobCatalogEntity crmCustomerSatisfaction = createEntity("客户满意度", "CRM系统的客户满意度", crmSystem);
        JobCatalogEntity crmCustomerRetention = createEntity("客户留存率", "CRM系统的客户留存率", crmSystem);
        rootEntities.add(crmSystem);
        rootEntities.add(crmCustomerSatisfaction);
        rootEntities.add(crmCustomerRetention);

        // 供应链管理系统
        JobCatalogEntity supplyChainManagement = createEntity("供应链管理系统", "供应链管理系统", rootEntity);
        JobCatalogEntity supplyChainEfficiency = createEntity("供应链效率", "供应链系统的效率", supplyChainManagement);
        JobCatalogEntity supplyChainCostAnalysis = createEntity("供应链成本", "供应链系统的成本", supplyChainManagement);
        rootEntities.add(supplyChainManagement);
        rootEntities.add(supplyChainEfficiency);
        rootEntities.add(supplyChainCostAnalysis);

        // 仓储管理系统
        JobCatalogEntity warehouseManagement = createEntity("仓储管理系统", "仓储管理系统", rootEntity);
        JobCatalogEntity warehouseInventoryAnalysis = createEntity("库存数据", "仓储系统的库存数据", warehouseManagement);
        JobCatalogEntity warehouseEfficiencyAnalysis = createEntity("仓储效率", "仓储系统的效率", warehouseManagement);
        rootEntities.add(warehouseManagement);
        rootEntities.add(warehouseInventoryAnalysis);
        rootEntities.add(warehouseEfficiencyAnalysis);

        // 生产制造系统
        JobCatalogEntity manufacturingSystem = createEntity("生产制造系统", "生产制造系统", rootEntity);
        JobCatalogEntity productionQualityAnalysis = createEntity("产品质量", "生产系统的质量数据", manufacturingSystem);
        JobCatalogEntity productionProcessAnalysis = createEntity("生产过程", "生产系统的生产过程", manufacturingSystem);
        rootEntities.add(manufacturingSystem);
        rootEntities.add(productionQualityAnalysis);
        rootEntities.add(productionProcessAnalysis);

        // 物流运输系统
        JobCatalogEntity logisticsSystem = createEntity("物流运输系统", "物流运输系统", rootEntity);
        JobCatalogEntity logisticsRouteAnalysis = createEntity("物流路线", "物流系统的路线", logisticsSystem);
        JobCatalogEntity logisticsDeliveryAnalysis = createEntity("配送效率", "物流系统的配送效率", logisticsSystem);
        rootEntities.add(logisticsSystem);
        rootEntities.add(logisticsRouteAnalysis);
        rootEntities.add(logisticsDeliveryAnalysis);

        // 市场营销系统
        JobCatalogEntity marketingSystem = createEntity("市场营销系统", "市场营销系统", rootEntity);
        JobCatalogEntity marketingCampaignAnalysis = createEntity("营销活动", "市场营销系统的活动", marketingSystem);
        JobCatalogEntity marketingTrendAnalysis = createEntity("市场趋势", "市场营销系统的趋势", marketingSystem);
        rootEntities.add(marketingSystem);
        rootEntities.add(marketingCampaignAnalysis);
        rootEntities.add(marketingTrendAnalysis);

        // 安全监控系统
        JobCatalogEntity securitySystem = createEntity("安全监控系统", "安全监控系统", rootEntity);
        JobCatalogEntity securityThreatAnalysis = createEntity("安全威胁", "安全系统的威胁", securitySystem);
        JobCatalogEntity securityComplianceAnalysis = createEntity("合规性", "安全系统的合规性", securitySystem);
        rootEntities.add(securitySystem);
        rootEntities.add(securityThreatAnalysis);
        rootEntities.add(securityComplianceAnalysis);

        promptCatalogService.saveBatch(rootEntities);
    }

    private JobCatalogEntity createEntity(String name, String description, JobCatalogEntity parent) {
        JobCatalogEntity entity = new JobCatalogEntity();

        entity.setId(IdUtil.getSnowflakeNextId());
        entity.setName(name);
        entity.setDescription(description);
        entity.setIcon("");
        entity.setOrderNum(1);
        entity.setParentId(parent.getId());
        entity.setChildren(new ArrayList<>());

        return entity ;
    }

}
