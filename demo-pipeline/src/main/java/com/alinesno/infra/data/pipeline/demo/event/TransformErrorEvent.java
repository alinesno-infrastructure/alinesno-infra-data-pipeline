package com.alinesno.infra.data.pipeline.demo.event;

public class TransformErrorEvent extends DataPipelineEvent {
    private final Object data;
    private final String transformerName;
    private final String errorMsg;

    public TransformErrorEvent(Object data, String transformerName, String errorMsg) {
        super("TRANSFORM_ERROR");
        this.data = data;
        this.transformerName = transformerName;
        this.errorMsg = errorMsg;
    }
}
