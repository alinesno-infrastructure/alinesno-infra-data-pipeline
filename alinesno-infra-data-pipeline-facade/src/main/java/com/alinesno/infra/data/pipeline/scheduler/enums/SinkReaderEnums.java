package com.alinesno.infra.data.pipeline.scheduler.enums;

import lombok.Getter;

/**
 * 数据源写入器枚举类
 * 用于表示不同的数据源类型
 */
@Getter
public enum SinkReaderEnums {

    CLICKHOUSE("clickhouse"), // ClickHouse数据源
    MINIO("minio"), // MinIO数据源
    MYSQL("mysql"), // MYSQL数据源
    DORIS("doris"); // Doris数据源

    /**
     * -- GETTER --
     *  获取数据源代码值
     *
     * @return 数据源代码值
     */
    private final String code;

    /**
     * 构造函数
     * @param code 数据源代码值
     */
    SinkReaderEnums(String code) {
        this.code = code;
    }

}
