package com.alinesno.infra.data.pipeline.demo.event;

// 写入事件
public class DataWriteEvent extends DataPipelineEvent {
    private final long writeCount;
    private final long totalCount;
    private final String sinkName;

    public DataWriteEvent(long writeCount, long totalCount, String sinkName) {
        super("DATA_WRITE");
        this.writeCount = writeCount;
        this.totalCount = totalCount;
        this.sinkName = sinkName;
    }
}