package com.alinesno.infra.data.pipeline.demo.event;

/**
 * 事件基类
 */
public abstract class DataPipelineEvent {
    private final long timestamp = System.currentTimeMillis();
    private final String eventType;

    public DataPipelineEvent(String eventType) {
        this.eventType = eventType;
    }

    // Getters
    public long getTimestamp() { return timestamp; }
    public String getEventType() { return eventType; }
}

