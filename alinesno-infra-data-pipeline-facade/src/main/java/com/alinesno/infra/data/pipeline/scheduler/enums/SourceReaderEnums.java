package com.alinesno.infra.data.pipeline.scheduler.enums;

import lombok.Getter;

/**
 * 数据源读取器枚举类
 * 用于表示不同的数据源类型
 */
@Getter
public enum SourceReaderEnums {

    MYSQL("mysql", "mysql.png", "开放源码的小型关系数据库", "SELECT 1"), // MySQL 数据源
    KAFKA("kafka", "kafka.png", "分布式流处理平台", "DESCRIBE TOPICS"), // Kafka 数据源

    S3FILE("s3file", "s3file.png", "Amazon S3 文件存储服务", "SELECT * FROM s3object LIMIT 1"), // S3 文件数据源
    MINIO("minio", "minio.png", "高性能对象存储服务", "SELECT * FROM minioobject LIMIT 1"), // MinIO 数据源
    QINIU("qiniu", "qiniu.png", "云存储服务提供商", "SELECT * FROM qiniuobject LIMIT 1"), // Qiniu 数据源

    FTP("ftp", "ftp.png", "文件传输协议", "ls"), // FTP 数据源
    SFTP("sftp", "sftp.png", "安全文件传输协议", "ls"), // SFTP 数据源
    CSV("csv", "csv.png", "逗号分隔值文件格式", "SELECT * FROM csv LIMIT 1"), // CSV 数据源
    EXCEL("excel", "excel.png", "Microsoft Excel 文件格式", "SELECT * FROM excel LIMIT 1"); // Excel 数据源

    /**
     * 数据源代码值
     */
    private final String code;

    /**
     * 图标路径
     */
    private final String icon;

    /**
     * 描述信息
     */
    private final String desc;

    /**
     * 验证查询
     */
    private final String validateQuery;

    /**
     * 构造函数
     *
     * @param code 数据源代码值
     * @param icon 图标路径
     * @param desc 描述信息
     * @param validateQuery 验证查询
     */
    SourceReaderEnums(String code, String icon, String desc, String validateQuery) {
        this.code = code;
        this.icon = icon;
        this.desc = desc;
        this.validateQuery = validateQuery;
    }

    /**
     * 获取到所有数据源类型List列表
     */
    public static String[] getAllSourceReaderCodes() {
        SourceReaderEnums[] values = SourceReaderEnums.values();
        String[] sourceReaderCodes = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            sourceReaderCodes[i] = values[i].getCode();
        }
        return sourceReaderCodes;
    }

    /**
     * 通过类型获取验证查询
     */
    public static String getValidateQuery(String type) {
        for (SourceReaderEnums sourceEnum : SourceReaderEnums.values()) {
            if (sourceEnum.getCode().equalsIgnoreCase(type)) {
                return sourceEnum.getValidateQuery();
            }
        }
        return null;
    }
}