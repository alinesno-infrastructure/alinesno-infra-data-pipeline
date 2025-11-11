package com.alinesno.infra.data.pipeline.demo.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DataSourceConfig {
    private String sourceName;
    private String sql;
    private Map<String, Object> params = new HashMap<>();
}