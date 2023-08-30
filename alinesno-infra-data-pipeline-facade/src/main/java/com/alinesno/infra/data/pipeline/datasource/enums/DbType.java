package com.alinesno.infra.data.pipeline.datasource.enums;

public enum DbType {
    MYSQL("mysql", "开源的小型数据库"),
    CLICKHOUSE("clickhouse", "ClickHouse数据库"),
    DORIS("doris", "Doris数据库"),
    ORACLE("oracle", "Oracle数据库"),
    ELASTICSEARCH("elasticsearch", "Elasticsearch数据库");

    private final String type;
    private final String description;

    DbType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
