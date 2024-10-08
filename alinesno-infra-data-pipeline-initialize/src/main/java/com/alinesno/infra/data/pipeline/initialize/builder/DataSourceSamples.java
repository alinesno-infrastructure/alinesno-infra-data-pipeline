package com.alinesno.infra.data.pipeline.initialize.builder;

import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import com.alinesno.infra.data.pipeline.enums.TransTypeEnums;
import com.alinesno.infra.data.pipeline.scheduler.enums.SinkReaderEnums;

import java.util.ArrayList;
import java.util.List;

/**
 * DataSourceSamples 类
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
public class DataSourceSamples {

    public static List<ReaderSourceEntity> createSampleDataSources() {
        List<ReaderSourceEntity> dataSources = new ArrayList<>();

        // MySQL 数据源
        ReaderSourceEntity mysqlDataSource = new ReaderSourceEntity();
        mysqlDataSource.setReaderName(SinkReaderEnums.MYSQL.name());
        mysqlDataSource.setReaderUrl("jdbc:mysql://localhost:3309/dev_alinesno_infra_data_pipeline_v100?serverTimezone=GMT%2B8&zeroDateTimeBehavior=CONVERT_TO_NULL");
        mysqlDataSource.setReaderUsername("root");
        mysqlDataSource.setReaderPasswd("adminer");
        mysqlDataSource.setReaderPort("3306");
        mysqlDataSource.setReaderType(SinkReaderEnums.MYSQL.getCode());
        mysqlDataSource.setReaderDesc("这是一个MySQL数据源示例。");
        mysqlDataSource.setOwner("大罗村社区");
        mysqlDataSource.setOperationType(TransTypeEnums.READER.getCode());
        dataSources.add(mysqlDataSource);

        // PostgreSQL 数据源
        ReaderSourceEntity postgresDataSource = new ReaderSourceEntity();
        postgresDataSource.setReaderName(SinkReaderEnums.POSTGRESQL.name());
        postgresDataSource.setReaderUrl("jdbc:postgresql://localhost:5432/mydatabase");
        postgresDataSource.setReaderUsername("postgres");
        postgresDataSource.setReaderPasswd("postgres");
        postgresDataSource.setReaderPort("5432");
        postgresDataSource.setReaderType(SinkReaderEnums.POSTGRESQL.getCode());
        postgresDataSource.setReaderDesc("这是一个PostgreSQL数据源示例。");
        postgresDataSource.setOwner("电商管理系统");
        postgresDataSource.setOperationType(TransTypeEnums.READER.getCode());
        dataSources.add(postgresDataSource);

        // Oracle 数据源
        ReaderSourceEntity oracleDataSource = new ReaderSourceEntity();
        oracleDataSource.setReaderName(SinkReaderEnums.MINIO.name());
        oracleDataSource.setReaderUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        oracleDataSource.setReaderUsername("scott");
        oracleDataSource.setReaderPasswd("tiger");
        oracleDataSource.setReaderPort("1521");
        oracleDataSource.setReaderType(SinkReaderEnums.MINIO.getCode());
        oracleDataSource.setReaderDesc("这是一个Oracle数据源示例。");
        oracleDataSource.setOwner("电商管理系统");
        oracleDataSource.setOperationType(TransTypeEnums.READER.getCode());
        dataSources.add(oracleDataSource);

        // SQL Server 数据源
        ReaderSourceEntity sqlServerDataSource = new ReaderSourceEntity();
        sqlServerDataSource.setReaderName(SinkReaderEnums.MINIO.name());
        sqlServerDataSource.setReaderUrl("jdbc:sqlserver://localhost:1433;databaseName=mydatabase");
        sqlServerDataSource.setReaderUsername("sa");
        sqlServerDataSource.setReaderPasswd("P@ssw0rd");
        sqlServerDataSource.setReaderPort("1433");
        sqlServerDataSource.setReaderType(SinkReaderEnums.MINIO.getCode());
        sqlServerDataSource.setReaderDesc("这是一个SQL Server数据源示例。");
        sqlServerDataSource.setOwner("大罗村社区");
        sqlServerDataSource.setOperationType(TransTypeEnums.READER.getCode());
        dataSources.add(sqlServerDataSource);

        // MongoDB 数据源
        ReaderSourceEntity mongoDataSource = new ReaderSourceEntity();
        mongoDataSource.setReaderName(SinkReaderEnums.REDIS.name());
        mongoDataSource.setReaderUrl("mongodb://localhost:27017/mydatabase");
        mongoDataSource.setReaderUsername("mongoUser");
        mongoDataSource.setReaderPasswd("mongoPassword");
        mongoDataSource.setReaderPort("27017");
        mongoDataSource.setReaderType(SinkReaderEnums.REDIS.getCode());
        mongoDataSource.setReaderDesc("这是一个MongoDB数据源示例。");
        mongoDataSource.setOwner("电商管理系统");
        mongoDataSource.setOperationType(TransTypeEnums.READER.getCode());
        dataSources.add(mongoDataSource);

        // Redis 数据源
        ReaderSourceEntity redisDataSource = new ReaderSourceEntity();
        redisDataSource.setReaderName(SinkReaderEnums.REDIS.name());
        redisDataSource.setReaderUrl("redis://localhost:6379/0");
        redisDataSource.setReaderUsername("");
        redisDataSource.setReaderPasswd("");
        redisDataSource.setReaderPort("6379");
        redisDataSource.setReaderType(SinkReaderEnums.REDIS.getCode());
        redisDataSource.setReaderDesc("这是一个Redis数据源示例。");
        redisDataSource.setOwner("大罗村社区");
        redisDataSource.setOperationType(TransTypeEnums.READER.getCode());
        dataSources.add(redisDataSource);

        // Kafka 数据源
        ReaderSourceEntity kafkaDataSource = new ReaderSourceEntity();
        kafkaDataSource.setReaderName(SinkReaderEnums.KAFKA.name());
        kafkaDataSource.setReaderUrl("kafka://localhost:9092");
        kafkaDataSource.setReaderUsername("");
        kafkaDataSource.setReaderPasswd("");
        kafkaDataSource.setReaderPort("9092");
        kafkaDataSource.setReaderType(SinkReaderEnums.KAFKA.getCode());
        kafkaDataSource.setReaderDesc("这是一个Kafka数据源示例。");
        kafkaDataSource.setOwner("电商管理系统");
        kafkaDataSource.setOperationType(TransTypeEnums.READER.getCode());
        dataSources.add(kafkaDataSource);

        // Elasticsearch 数据源
        ReaderSourceEntity elasticsearchDataSource = new ReaderSourceEntity();
        elasticsearchDataSource.setReaderName(SinkReaderEnums.ELASTICSEARCH.name());
        elasticsearchDataSource.setReaderUrl("elasticsearch://localhost:9200");
        elasticsearchDataSource.setReaderUsername("");
        elasticsearchDataSource.setReaderPasswd("");
        elasticsearchDataSource.setReaderPort("9200");
        elasticsearchDataSource.setReaderType(SinkReaderEnums.ELASTICSEARCH.getCode());
        elasticsearchDataSource.setReaderDesc("这是一个Elasticsearch数据源示例。");
        elasticsearchDataSource.setOwner("大罗村社区");
        elasticsearchDataSource.setOperationType(TransTypeEnums.READER.getCode());
        dataSources.add(elasticsearchDataSource);

        // Hive 数据源
        ReaderSourceEntity hiveDatasource = new ReaderSourceEntity();
        hiveDatasource.setReaderName(SinkReaderEnums.HIVE.name());
        hiveDatasource.setReaderUrl("elasticsearch://localhost:9200");
        hiveDatasource.setReaderUsername("");
        hiveDatasource.setReaderPasswd("");
        hiveDatasource.setReaderPort("9200");
        hiveDatasource.setReaderType(SinkReaderEnums.HIVE.getCode());
        hiveDatasource.setReaderDesc("这是一个Hive数据源示例。");
        hiveDatasource.setOwner("大罗村社区");
        hiveDatasource.setOperationType(TransTypeEnums.WRITER.getCode());
        dataSources.add(hiveDatasource);

        // 七牛数据源
        ReaderSourceEntity qiniuDatasource = new ReaderSourceEntity();
        qiniuDatasource.setReaderName(SinkReaderEnums.QINIU.name());
        qiniuDatasource.setReaderUrl("elasticsearch://localhost:9200");
        qiniuDatasource.setReaderUsername("");
        qiniuDatasource.setReaderPasswd("");
        qiniuDatasource.setReaderPort("9200");
        qiniuDatasource.setReaderType(SinkReaderEnums.QINIU.getCode());
        qiniuDatasource.setReaderDesc("这是一个七牛数据源示例。");
        qiniuDatasource.setOwner("大罗村社区");
        qiniuDatasource.setOperationType(TransTypeEnums.WRITER.getCode());
        dataSources.add(qiniuDatasource);

        return dataSources;
    }

}