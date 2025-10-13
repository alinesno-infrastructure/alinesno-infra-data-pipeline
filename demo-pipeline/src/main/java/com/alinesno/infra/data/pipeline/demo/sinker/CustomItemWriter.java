package com.alinesno.infra.data.pipeline.demo.sinker;

import com.alinesno.infra.data.pipeline.demo.base.AbstractDataSinker;
import com.alinesno.infra.data.pipeline.demo.config.SinkConfig;
import com.alinesno.infra.data.pipeline.demo.event.DataWriteEvent;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import org.springframework.batch.item.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomItemWriter implements ItemWriter<java.util.Map<String,Object>>, ItemStream {
    private final AbstractDataSinker<Map<String,Object>> sinker;
    private final int batchSize;
    private final List<java.util.Map<String,Object>> buffer = new ArrayList<>();

    public CustomItemWriter(AbstractDataSinker<java.util.Map<String,Object>> sinker, int batchSize) {
        this.sinker = sinker;
        this.batchSize = batchSize;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try { sinker.init(); } catch (Exception e) { throw new ItemStreamException(e); }
    }

    @Override public void update(ExecutionContext executionContext) {}
    @Override
    public void close() throws ItemStreamException {
        try {
            if (!buffer.isEmpty()) {
                sinker.batchWrite(new ArrayList<>(buffer));
                EventPublisher.publish(new DataWriteEvent(buffer.size(), -1,
                        sinker.config instanceof SinkConfig ?
                                ((SinkConfig) sinker.config).getSinkName() : "unknown"));
                buffer.clear();
            }
            sinker.commit();
            sinker.close();
        } catch (Exception e) { throw new ItemStreamException(e); }
    }

    @Override
    public void write(Chunk<? extends Map<String, Object>> items) throws Exception {
        buffer.addAll((Collection<? extends Map<String, Object>>) items);
        if (buffer.size() >= batchSize) {
            sinker.batchWrite(new ArrayList<>(buffer));
            EventPublisher.publish(new DataWriteEvent(buffer.size(), -1,
                    sinker.config instanceof SinkConfig ?
                            ((SinkConfig) sinker.config).getSinkName() : "unknown"));
            buffer.clear();
        }
    }
}