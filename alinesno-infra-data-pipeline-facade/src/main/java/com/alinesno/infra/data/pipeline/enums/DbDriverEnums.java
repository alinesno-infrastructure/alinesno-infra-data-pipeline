package com.alinesno.infra.data.pipeline.enums;

import lombok.Getter;

@Getter
public enum DbDriverEnums {

    MYSQL("mysql", "MySQL", "com.mysql.cj.jdbc.Driver", "fa-brands fa-mysql"),
    ORACLE("oracle", "Oracle", "oracle.jdbc.driver.OracleDriver", "fa-brands fa-oracle"),
    SQLSERVER("sqlserver", "SQLServer", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "fa-brands fa-microsoft"),
    POSTGRESQL("postgresql", "PostgreSQL", "org.postgresql.Driver", "fa-brands fa-postgresql"),
    DB2("db2", "DB2", "com.ibm.db2.jcc.DB2Driver", "fa-brands fa-ibm"),
    MONGODB("mongodb", "MongoDB", "com.mongodb.jdbc.MongoDriver", "fa-brands fa-mongodb"),
    CASSANDRA("cassandra", "Cassandra", "com.datastax.oss.driver.internal.core.cql.CqlRequestHandler", "fa-brands fa-cassandra"),
    ELASTICSEARCH("elasticsearch", "Elasticsearch", "org.elasticsearch.client.jdbc.ElasticsearchDriver", "fa-brands fa-elastic"),
    HBASE("hbase", "HBase", "org.apache.hadoop.hbase.jdbc.HBaseDriver", "fa-brands fa-hadoop"),
    HIVE("hive", "Hive", "org.apache.hive.jdbc.HiveDriver", "fa-brands fa-hive"),
    KAFKA("kafka", "Kafka", "org.apache.kafka.jdbc.KafkaDriver", "fa-brands fa-kafka"),
    REDIS("redis", "Redis", "redis.clients.jedis.JedisDriver", "fa-brands fa-redis"),
    S3("s3", "S3", "com.amazonaws.jdbc.AWSJDBCDriver", "fa-brands fa-amazon"),
    SFTP("sftp", "SFTP", "com.jcraft.jsch.jdbc.JSchDriver", "fa-solid fa-upload"),
    FTP("ftp", "FTP", "com.enterprisedt.net.ftp.FTPDriver", "fa-solid fa-upload"),
    HTTP("http", "HTTP", "org.apache.http.jdbc.HttpDriver", "fa-solid fa-globe"),
    WEBSERVICE("webservice", "WebService", "org.apache.axis.jdbc.AxisDriver", "fa-solid fa-cloud"),
    JSON("json", "JSON", "org.json.JSONDriver", "fa-solid fa-file-json"),
    XML("xml", "XML", "org.w3c.dom.jdbc.DomDriver", "fa-solid fa-file-xml"),
    CSV("csv", "CSV", "org.apache.commons.csv.CsvDriver", "fa-solid fa-file-csv"),
    EXCEL("excel", "Excel", "org.apache.poi.jdbc.ExcelDriver", "fa-solid fa-file-excel")
            ;

    private final String driverName;
    private final String dbType;
    private final String dbDriver;
    private final String icon; // 数据库图标

    DbDriverEnums(String driverName, String dbType, String dbDriver, String icon) {
        this.driverName = driverName;
        this.dbType = dbType;
        this.dbDriver = dbDriver;
        this.icon = icon;
    }

    /**
     * 通过类型获取到驱动名称
     */
    public static String getDriverName(String type) {
        for (DbDriverEnums dbEnum : DbDriverEnums.values()) {
            if (dbEnum.getDbType().equals(type)) {
                return dbEnum.getDriverName();
            }
        }
        return null;
    }

    /**
     * 通过类型获取到数据库驱动
     */
    public static String getDbDriver(String type) {
        for (DbDriverEnums dbEnum : DbDriverEnums.values()) {
            if (dbEnum.getDbType().equalsIgnoreCase(type)) {
                return dbEnum.getDbDriver();
            }
        }
        return null;
    }

}