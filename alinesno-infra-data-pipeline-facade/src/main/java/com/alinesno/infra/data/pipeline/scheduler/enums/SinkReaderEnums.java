package com.alinesno.infra.data.pipeline.scheduler.enums;

import lombok.Getter;

/**
 * 数据源写入器枚举类
 * 用于表示不同的数据源类型
 */
@Getter
public enum SinkReaderEnums {

    CLICKHOUSE("clickhouse", "clickhouse.png"), // ClickHouse 数据源
    MINIO("minio", "minio.png"), // MinIO 数据源
    MYSQL("mysql", "mysql.png"), // MySQL 数据源
    DORIS("doris", "doris.png"), // Doris 数据源
    DB2("db2", "db2.png"), // DB2 数据源
    GREENPLUM("greenplum", "greenplum.png"), // Greenplum 数据源
    HIVE("hive", "hive.png"), // Hive 数据源
    POSTGRESQL("postgresql", "postgresql.png"), // PostgreSQL 数据源
    SQLSERVER("sqlserver", "sqlserver.png"), // SQL Server 数据源
    ORACLE("oracle", "oracle.png"), // Oracle 数据源
    REDIS("redis", "redis.png"), // Redis 数据源
    MONGODB("mongodb", "mongodb.png"), // MongoDB 数据源
    ELASTICSEARCH("elasticsearch", "elasticsearch.png"), // Elasticsearch 数据源
    KAFKA("kafka", "kafka.png"), // Kafka 数据源
    S3FILE("s3file", "s3file.png"), // S3 文件数据源
    FTP("ftp", "ftp.png"), // FTP 数据源
    SFTP("sftp", "sftp.png"), // SFTP 数据源
    LOCALFILE("localfile", "localfile.png"), // 本地文件数据源
    CSV("csv", "csv.png"), // CSV 数据源
    EXCEL("excel", "excel.png"), // Excel 数据源
    JSON("json", "json.png"), // JSON 数据源
    XML("xml", "xml.png"), // XML 数据源
    HTTP("http", "http.png"), // HTTP 数据源
    HBASE("hbase", "hbase.png"), // HBase 数据源
    HADOOP("hadoop", "hadoop.png"), // Hadoop 数据源
    KUDU("kudu", "kudu.png"), // Kudu 数据源
    QINIU("qiniu", "qiniu.png"), // Qiniu 数据源
    STARROCKS("starrocks", "starrocks.png"); // StarRocks 数据源

    /**
     * -- GETTER --
     *  获取数据源代码值
     *
     * @return 数据源代码值
     */
    private final String code;
    private final String icon ;

    /**
     * 构造函数
     *
     * @param code 数据源代码值
     * @param icon
     */
    SinkReaderEnums(String code, String icon) {
        this.code = code;
        this.icon = icon;
    }

    /**
     * 获取到所有数据源类型List列表
     */
    public static String[] getAllSourceReader() {
        SinkReaderEnums[] values = SinkReaderEnums.values();
        String[] sourceReader = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            sourceReader[i] = values[i].getCode();
        }
        return sourceReader;
    }

}
