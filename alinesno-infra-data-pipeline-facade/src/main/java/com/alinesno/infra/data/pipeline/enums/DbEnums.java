package com.alinesno.infra.data.pipeline.enums;

import lombok.Getter;

@Getter
public enum DbEnums {

    MYSQL("mysql", "MySQL", "com.mysql.cj.jdbc.Driver", "fa-brands fa-mysql"),
    ORACLE("oracle", "Oracle", "oracle.jdbc.driver.OracleDriver", "fa-brands fa-java"),
    SQLSERVER("sqlserver", "SQLServer", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "fa-brands fa-windows"),
    POSTGRESQL("postgresql", "PostgreSQL", "org.postgresql.Driver", "fa-brands fa-postgres"),
    DB2("db2", "DB2", "com.ibm.db2.jcc.DB2Driver", "fa-brands fa-linux"),
    MONGODB("mongodb", "MongoDB", "com.mongodb.jdbc.MongoDriver", "fa-brands fa-mongodb"),
    CASSANDRA("cassandra", "Cassandra", "com.datastax.oss.driver.internal.core.cql.CqlRequestHandler", "fa-solid fa-database"),
    ELASTICSEARCH("elasticsearch", "Elasticsearch", "org.elasticsearch.client.jdbc.ElasticsearchDriver", "fa-brands fa-elastic"),
    HBASE("hbase", "HBase", "org.apache.hadoop.hbase.jdbc.HBaseDriver", "fa-solid fa-database"),
    HIVE("hive", "Hive", "org.apache.hive.jdbc.HiveDriver", "fa-solid fa-database"),
    KAFKA("kafka", "Kafka", "org.apache.kafka.jdbc.KafkaDriver", "fa-brands fa-apache"),
    KUDU("kudu", "Kudu", "org.apache.kudu.jdbc.KuduDriver", "fa-solid fa-database"),
    REDIS("redis", "Redis", "redis.clients.jedis.JedisDriver", "fa-brands fa-redis"),
    S3("s3", "S3", "com.amazonaws.jdbc.AWSJDBCDriver", "fa-brands fa-amazon"),
    SFTP("sftp", "SFTP", "com.jcraft.jsch.jdbc.JSchDriver", "fa-solid fa-upload"),
    FTP("ftp", "FTP", "com.enterprisedt.net.ftp.FTPDriver", "fa-solid fa-upload"),
    MQTT("mqtt", "MQTT", "org.eclipse.paho.client.mqttv3.MqttDriver", "fa-solid fa-rss"),
    MQ("mq", "MQ", "com.ibm.mq.jdbc.MQDriver", "fa-solid fa-rss"),
    HTTP("http", "HTTP", "org.apache.http.jdbc.HttpDriver", "fa-solid fa-globe"),
    WEBSERVICE("webservice", "WebService", "org.apache.axis.jdbc.AxisDriver", "fa-solid fa-cloud"),
    JDBC("jdbc", "JDBC", "org.apache.derby.jdbc.EmbeddedDriver", "fa-brands fa-java"),
    JSON("json", "JSON", "org.json.JSONDriver", "fa-solid fa-file-code"),
    XML("xml", "XML", "org.w3c.dom.jdbc.DomDriver", "fa-solid fa-file-code"),
    CSV("csv", "CSV", "org.apache.commons.csv.CsvDriver", "fa-solid fa-file-csv"),
    FIXED("fixed", "Fixed", "org.apache.commons.fixed.FixedDriver", "fa-solid fa-file-code"),
    AVRO("avro", "Avro", "org.apache.avro.jdbc.AvroDriver", "fa-solid fa-file-code"),
    PARQUET("parquet", "Parquet", "org.apache.parquet.jdbc.ParquetDriver", "fa-solid fa-file-code"),
    ORC("orc", "ORC", "org.apache.orc.jdbc.OrcDriver", "fa-solid fa-file-code"),
    EXCEL("excel", "Excel", "org.apache.poi.jdbc.ExcelDriver", "fa-solid fa-file-excel"),
    LUCENE("lucene", "Lucene", "org.apache.lucene.jdbc.LuceneDriver", "fa-solid fa-search")

    ;

    private final String driverName;
    private final String dbType;
    private final String dbDriver;
    private final String icon; // 数据库图标

    DbEnums(String driverName, String dbType, String dbDriver, String icon) {
        this.driverName = driverName;
        this.dbType = dbType;
        this.dbDriver = dbDriver;
        this.icon = icon;
    }

    /**
     * 通过类型获取到驱动名称
     */
    public static String getDriverName(String type) {
        for (DbEnums dbEnum : DbEnums.values()) {
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
        for (DbEnums dbEnum : DbEnums.values()) {
            if (dbEnum.getDbType().equals(type)) {
                return dbEnum.getDbDriver();
            }
        }
        return null;
    }

}