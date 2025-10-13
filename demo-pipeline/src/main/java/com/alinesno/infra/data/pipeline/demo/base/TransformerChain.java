package com.alinesno.infra.data.pipeline.demo.base;

import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import com.alinesno.infra.data.pipeline.demo.event.TransformSuccessEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 转换链管理器（支持多插件组合）
public class TransformerChain {
    private final List<DataTransformer> transformers = new ArrayList<>();

    public TransformerChain addTransformer(DataTransformer transformer) {
        transformers.add(transformer);
        return this;
    }

    public Map<String,Object> execute(Map<String,Object> row) {
        Map<String,Object> current = row;
        for (DataTransformer transformer : transformers) {
            try {
                row = transformer.transform(row);
                EventPublisher.publish(new TransformSuccessEvent(current, transformer.getName()));
            } catch (Exception e) {
                current = transformer.handleError(current, e);
            }
        }
        return current;
    }
}