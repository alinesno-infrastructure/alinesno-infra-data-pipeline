package com.alinesno.infra.data.pipeline.initialize.builder;

import com.alinesno.infra.data.pipeline.entity.JobEntity;
import com.alinesno.infra.data.pipeline.enums.DbDriverEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DataInitializer {

    public static List<JobEntity> initializeJobs() {
        JobEntity[] jobs = new JobEntity[]{
                createJob("订单数据同步", "将订单表中的数据从MySQL同步到PostgreSQL", "1", DbDriverEnums.MYSQL, 1L, 100, DbDriverEnums.POSTGRESQL, 2L, 50),
                createJob("财务报表迁移", "迁移Oracle数据库中的财务报表数据到MySQL", "2", DbDriverEnums.ORACLE, 2L, 200, DbDriverEnums.MYSQL, 3L, 100),
                createJob("销售数据复制", "从SQL Server复制销售数据到MySQL", "0", DbDriverEnums.SQLSERVER, 3L, 150, DbDriverEnums.MYSQL, 4L, 75),
                createJob("客户信息整合", "整合PostgreSQL中的客户信息到SQL Server", "3", DbDriverEnums.POSTGRESQL, 4L, 125, DbDriverEnums.SQLSERVER, 5L, 80),
                createJob("库存数据备份", "备份MySQL中的库存数据到Oracle", "1", DbDriverEnums.MYSQL, 5L, 90, DbDriverEnums.ORACLE, 6L, 120),
                createJob("员工信息清洗", "清洗Oracle数据库中的员工信息数据到PostgreSQL", "2", DbDriverEnums.ORACLE, 6L, 225, DbDriverEnums.POSTGRESQL, 7L, 110),
                createJob("产品数据转换", "从SQL Server转换产品数据到MySQL", "0", DbDriverEnums.SQLSERVER, 7L, 175, DbDriverEnums.MYSQL, 8L, 95),
                createJob("市场分析验证", "验证PostgreSQL中的市场分析数据到SQL Server", "3", DbDriverEnums.POSTGRESQL, 8L, 145, DbDriverEnums.SQLSERVER, 9L, 85),
                createJob("物流信息加载", "加载MySQL中的物流信息到Oracle", "1", DbDriverEnums.MYSQL, 9L, 110, DbDriverEnums.ORACLE, 10L, 130)
        };

        AtomicLong id = new AtomicLong(1L);
        Arrays.stream(jobs).forEach(job -> {
           job.setId(id.getAndIncrement());
        }) ;

        return new ArrayList<>(Arrays.asList(jobs));
    }

    private static JobEntity createJob(String jobName,
                                       String jobDesc,
                                       String status,
                                       DbDriverEnums sourceDbType,
                                       long sourceDbId,
                                       int sourceDbBatchSize,
                                       DbDriverEnums targetDbType,
                                       long targetDbId,
                                       int targetDbBatchSize) {
        JobEntity job = new JobEntity();
        job.setJobName(jobName);
        job.setJobDesc(jobDesc);
        job.setStatus(status);
        job.setSourceDbType(sourceDbType.name());
        job.setSourceDbId(sourceDbId);
        job.setSourceDbBatchSize(sourceDbBatchSize);
        job.setTargetDbType(targetDbType.name());
        job.setTargetDbId(targetDbId);
        job.setTargetDbBatchSize(targetDbBatchSize);
        return job;
    }
}