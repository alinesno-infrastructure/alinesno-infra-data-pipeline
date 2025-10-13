package com.alinesno.infra.data.pipeline.demo.config;

public class SinkConfig {
    private String sinkName;
    private String table;
    private boolean upsert = true;

    public String getSinkName() { return sinkName; }
    public void setSinkName(String sinkName) { this.sinkName = sinkName; }
    public String getTable() { return table; }
    public void setTable(String table) { this.table = table; }
    public boolean isUpsert() { return upsert; }
    public void setUpsert(boolean upsert) { this.upsert = upsert; }
}