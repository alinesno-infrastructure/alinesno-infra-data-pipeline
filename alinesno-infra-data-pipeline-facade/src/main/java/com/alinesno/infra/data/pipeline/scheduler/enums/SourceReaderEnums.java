package com.alinesno.infra.data.pipeline.scheduler.enums;

/**
 * 数据源读取器枚举类
 * 用于表示不同的数据源类型
 */
public enum SourceReaderEnums {

    KAFKA("kafka"), // Kafka数据源
    HTTP("http"), // HTTP数据源
    MYSQL("mysql"), // MySQL数据源
    CLICKHOUSE("clickhouse"), // ClickHouse数据源
    ORACLE("oracle"), // Oracle数据源
    FTP("ftp"), // FTP数据源
    ELASTICSEARCH("elasticsearch"), // Elasticsearch数据源
    MINIO("minio"), // MinIO数据源
    HIVE("hive"), // Hive数据源
    REDIS("redis"), // Redis数据源
    QINIU("qiniu"); // Qiniu数据源

    private final String code;

    /**
     * 构造函数
     * @param code 数据源代码值
     */
    SourceReaderEnums(String code) {
        this.code = code;
    }

    /**
     * 获取数据源代码值
     * @return 数据源代码值
     */
    public String getCode() {
        return code;
    }
}
