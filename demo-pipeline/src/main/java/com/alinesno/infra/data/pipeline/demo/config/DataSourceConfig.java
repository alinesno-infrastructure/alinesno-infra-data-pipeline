package com.alinesno.infra.data.pipeline.demo.config;

import java.util.HashMap;
import java.util.Map;

public class DataSourceConfig {
    private String sourceName;
    private String sql;
    private Map<String, Object> params = new HashMap<>();

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    public String getSql() { return sql; }
    public void setSql(String sql) { this.sql = sql; }
    public Map<String, Object> getParams() { return params; }
    public void setParams(Map<String, Object> params) { this.params = params; }
}