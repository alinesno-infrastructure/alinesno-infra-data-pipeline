package com.alinesno.infra.data.pipeline.demo.event;

import com.alinesno.infra.data.pipeline.demo.event.DataPipelineEvent;

// 读取事件
public class DataReadEvent extends DataPipelineEvent {
    private final long readCount;
    private final long totalCount;
    private final String sourceName;

    public DataReadEvent(long readCount, long totalCount, String sourceName) {
        super("DATA_READ");
        this.readCount = readCount;
        this.totalCount = totalCount;
        this.sourceName = sourceName;
    }
}
