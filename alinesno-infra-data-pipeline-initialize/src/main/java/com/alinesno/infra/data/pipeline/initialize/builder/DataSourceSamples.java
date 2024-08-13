package com.alinesno.infra.data.pipeline.initialize.builder;

import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import com.alinesno.infra.data.pipeline.enums.DbEnums;

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
        mysqlDataSource.setReaderName("MySQL Data Source");
        mysqlDataSource.setReaderUrl("jdbc:mysql://localhost:3306/mydatabase");
        mysqlDataSource.setJreadercUrl("jdbc:mysql://localhost:3306/mydatabase");
        mysqlDataSource.setReaderUsername("root");
        mysqlDataSource.setReaderPasswd("password");
        mysqlDataSource.setReaderPort("3306");
        mysqlDataSource.setReaderType(DbEnums.MYSQL.getDbType());
        mysqlDataSource.setReaderDesc("这是一个MySQL数据源示例。");
        mysqlDataSource.setAuthor("约翰·杜");
        dataSources.add(mysqlDataSource);

        // PostgreSQL 数据源
        ReaderSourceEntity postgresDataSource = new ReaderSourceEntity();
        postgresDataSource.setReaderName("PostgreSQL Data Source");
        postgresDataSource.setReaderUrl("jdbc:postgresql://localhost:5432/mydatabase");
        postgresDataSource.setJreadercUrl("jdbc:postgresql://localhost:5432/mydatabase");
        postgresDataSource.setReaderUsername("postgres");
        postgresDataSource.setReaderPasswd("postgres");
        postgresDataSource.setReaderPort("5432");
        postgresDataSource.setReaderType(DbEnums.POSTGRESQL.getDbType());
        postgresDataSource.setReaderDesc("这是一个PostgreSQL数据源示例。");
        postgresDataSource.setAuthor("简·杜");
        dataSources.add(postgresDataSource);

        // Oracle 数据源
        ReaderSourceEntity oracleDataSource = new ReaderSourceEntity();
        oracleDataSource.setReaderName("Oracle Data Source");
        oracleDataSource.setReaderUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        oracleDataSource.setJreadercUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        oracleDataSource.setReaderUsername("scott");
        oracleDataSource.setReaderPasswd("tiger");
        oracleDataSource.setReaderPort("1521");
        oracleDataSource.setReaderType(DbEnums.ORACLE.getDbType());
        oracleDataSource.setReaderDesc("这是一个Oracle数据源示例。");
        oracleDataSource.setAuthor("简·杜");
        dataSources.add(oracleDataSource);

        // SQL Server 数据源
        ReaderSourceEntity sqlServerDataSource = new ReaderSourceEntity();
        sqlServerDataSource.setReaderName("SQL Server Data Source");
        sqlServerDataSource.setReaderUrl("jdbc:sqlserver://localhost:1433;databaseName=mydatabase");
        sqlServerDataSource.setJreadercUrl("jdbc:sqlserver://localhost:1433;databaseName=mydatabase");
        sqlServerDataSource.setReaderUsername("sa");
        sqlServerDataSource.setReaderPasswd("P@ssw0rd");
        sqlServerDataSource.setReaderPort("1433");
        sqlServerDataSource.setReaderType(DbEnums.SQLSERVER.getDbType());
        sqlServerDataSource.setReaderDesc("这是一个SQL Server数据源示例。");
        sqlServerDataSource.setAuthor("约翰·杜");
        dataSources.add(sqlServerDataSource);

        // MongoDB 数据源
        ReaderSourceEntity mongoDataSource = new ReaderSourceEntity();
        mongoDataSource.setReaderName("MongoDB Data Source");
        mongoDataSource.setReaderUrl("mongodb://localhost:27017/mydatabase");
        mongoDataSource.setJreadercUrl("mongodb://localhost:27017/mydatabase");
        mongoDataSource.setReaderUsername("mongoUser");
        mongoDataSource.setReaderPasswd("mongoPassword");
        mongoDataSource.setReaderPort("27017");
        mongoDataSource.setReaderType(DbEnums.MONGODB.getDbType());
        mongoDataSource.setReaderDesc("这是一个MongoDB数据源示例。");
        mongoDataSource.setAuthor("简·杜");
        dataSources.add(mongoDataSource);

        // Redis 数据源
        ReaderSourceEntity redisDataSource = new ReaderSourceEntity();
        redisDataSource.setReaderName("Redis Data Source");
        redisDataSource.setReaderUrl("redis://localhost:6379/0");
        redisDataSource.setJreadercUrl("redis://localhost:6379/0");
        redisDataSource.setReaderUsername("");
        redisDataSource.setReaderPasswd("");
        redisDataSource.setReaderPort("6379");
        redisDataSource.setReaderType(DbEnums.REDIS.getDbType());
        redisDataSource.setReaderDesc("这是一个Redis数据源示例。");
        redisDataSource.setAuthor("约翰·杜");
        dataSources.add(redisDataSource);

        // Kafka 数据源
        ReaderSourceEntity kafkaDataSource = new ReaderSourceEntity();
        kafkaDataSource.setReaderName("Kafka Data Source");
        kafkaDataSource.setReaderUrl("kafka://localhost:9092");
        kafkaDataSource.setJreadercUrl("kafka://localhost:9092");
        kafkaDataSource.setReaderUsername("");
        kafkaDataSource.setReaderPasswd("");
        kafkaDataSource.setReaderPort("9092");
        kafkaDataSource.setReaderType(DbEnums.KAFKA.getDbType());
        kafkaDataSource.setReaderDesc("这是一个Kafka数据源示例。");
        kafkaDataSource.setAuthor("简·杜");
        dataSources.add(kafkaDataSource);

        // Elasticsearch 数据源
        ReaderSourceEntity elasticsearchDataSource = new ReaderSourceEntity();
        elasticsearchDataSource.setReaderName("Elasticsearch Data Source");
        elasticsearchDataSource.setReaderUrl("elasticsearch://localhost:9200");
        elasticsearchDataSource.setJreadercUrl("elasticsearch://localhost:9200");
        elasticsearchDataSource.setReaderUsername("");
        elasticsearchDataSource.setReaderPasswd("");
        elasticsearchDataSource.setReaderPort("9200");
        elasticsearchDataSource.setReaderType(DbEnums.ELASTICSEARCH.getDbType());
        elasticsearchDataSource.setReaderDesc("这是一个Elasticsearch数据源示例。");
        elasticsearchDataSource.setAuthor("约翰·杜");
        dataSources.add(elasticsearchDataSource);

        return dataSources;
    }

}