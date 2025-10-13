package com.alinesno.infra.data.pipeline.demo.event;

// 转换事件（成功/失败）
public class TransformSuccessEvent extends DataPipelineEvent {
    private final Object data;
    private final String transformerName;

    public TransformSuccessEvent(Object data, String transformerName) {
        super("TRANSFORM_SUCCESS");
        this.data = data;
        this.transformerName = transformerName;
    }
}

