package com.alinesno.infra.data.pipeline.demo.simple;

import com.alinesno.infra.data.pipeline.demo.base.AbstractDataReader;
import com.alinesno.infra.data.pipeline.demo.base.AbstractDataSinker;
import com.alinesno.infra.data.pipeline.demo.config.DataSourceConfig;
import com.alinesno.infra.data.pipeline.demo.config.SinkConfig;
import com.alinesno.infra.data.pipeline.demo.transformer.ClearNullTransformer;
import com.example.pipeline.io.mysql.MySQLDataReader;
import com.example.pipeline.io.mysql.MySQLDataSinker;
import com.alinesno.infra.data.pipeline.demo.reader.CustomItemReader;
import com.alinesno.infra.data.pipeline.demo.sinker.CustomItemWriter;
import com.alinesno.infra.data.pipeline.demo.base.DataTransformer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.*;

@Configuration
public class BatchConfig {

    // JdbcTemplate
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }

    // Source config
    @Bean
    public DataSourceConfig dataSourceConfig() {
        DataSourceConfig cfg = new DataSourceConfig();
        cfg.setSourceName("source_table");
        cfg.setSql("SELECT id, name, email, tags, created_at FROM source_table");
        return cfg;
    }

    // Sink config
    @Bean
    public SinkConfig sinkConfig() {
        SinkConfig cfg = new SinkConfig();
        cfg.setSinkName("target_table");
        cfg.setTable("target_table");
        cfg.setUpsert(true);
        return cfg;
    }

    // 实际的 reader/sinker
    @Bean
    public AbstractDataReader<Map<String,Object>> mysqlDataReader(DataSourceConfig cfg, JdbcTemplate jdbcTemplate) {
        return new MySQLDataReader(cfg, jdbcTemplate);
    }

    @Bean
    public AbstractDataSinker<Map<String,Object>> mysqlDataSinker(SinkConfig cfg, JdbcTemplate jdbcTemplate) {
        return new MySQLDataSinker(cfg, jdbcTemplate, 100);
    }

    // 包装为 Spring Batch 的 ItemReader / ItemWriter
    @Bean
    public CustomItemReader customItemReader(AbstractDataReader<Map<String,Object>> reader) {
        return new CustomItemReader(reader);
    }

    @Bean
    public CustomItemWriter customItemWriter(AbstractDataSinker<Map<String,Object>> sinker) {
        return new CustomItemWriter(sinker, 2); // writer 内部触发批大小 = 2
    }

    // Transformer 列表
    @Bean
    public List<DataTransformer> transformers() {
        List<DataTransformer> list = new ArrayList<>();
        list.add(new ClearNullTransformer());
        return list;
    }

    // ItemProcessor：依次调用 transformers
    @Bean
    public org.springframework.batch.item.ItemProcessor<Map<String,Object>, Map<String,Object>> transformerItemProcessor(
            List<DataTransformer> transformers) {

        return item -> {
            Map<String,Object> current = item;
            for (DataTransformer t : transformers) {
                try {
                    current = t.transform(current);
                    if (current == null) return null; // 过滤掉
                } catch (Exception e) {
                    current = t.handleError(current, e);
                    if (current == null) return null;
                }
            }
            return current;
        };
    }

    // Step（使用 StepBuilder）
    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      CustomItemReader reader,
                      org.springframework.batch.item.ItemProcessor<Map<String,Object>, Map<String,Object>> processor,
                      CustomItemWriter writer) {

        return new StepBuilder("step1", jobRepository)
                .<Map<String,Object>, Map<String,Object>>chunk(2, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // Job（使用 JobBuilder）
    @Bean
    public Job demoJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("demoJob", jobRepository)
                .start(step1)
                .build();
    }
}