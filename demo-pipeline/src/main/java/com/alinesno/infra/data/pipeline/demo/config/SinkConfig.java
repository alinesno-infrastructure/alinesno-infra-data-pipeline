package com.alinesno.infra.data.pipeline.demo.config;

import lombok.Data;

@Data
public class SinkConfig {
    private String sinkName;
    private String table;
    private boolean upsert = true;
}