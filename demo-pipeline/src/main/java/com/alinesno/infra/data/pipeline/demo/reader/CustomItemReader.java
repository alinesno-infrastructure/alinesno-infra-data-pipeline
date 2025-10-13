package com.alinesno.infra.data.pipeline.demo.reader;

import com.alinesno.infra.data.pipeline.demo.base.AbstractDataReader;
import com.alinesno.infra.data.pipeline.demo.config.DataSourceConfig;
import com.alinesno.infra.data.pipeline.demo.event.DataReadEvent;
import com.alinesno.infra.data.pipeline.demo.event.EventPublisher;
import org.springframework.batch.item.*;

import java.util.Iterator;
import java.util.Map;

public class CustomItemReader implements ItemReader<java.util.Map<String,Object>>, ItemStream {
    private final AbstractDataReader<Map<String,Object>> reader;
    private Iterator<java.util.Map<String,Object>> iterator;

    public CustomItemReader(AbstractDataReader<java.util.Map<String,Object>> reader) {
        this.reader = reader;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            reader.init();
            iterator = reader.read();
        } catch (Exception e) { throw new ItemStreamException(e); }
    }

    @Override
    public java.util.Map<String,Object> read() {
        if (iterator != null && iterator.hasNext()) {
            java.util.Map<String,Object> r = iterator.next();
            EventPublisher.publish(new DataReadEvent(1, -1, reader.config instanceof DataSourceConfig ?
                    ((DataSourceConfig) reader.config).getSourceName() : "unknown"));
            return r;
        }
        return null;
    }

    @Override public void update(ExecutionContext executionContext) {}
    @Override
    public void close() throws ItemStreamException {
        try { reader.close(); } catch (Exception e) { throw new ItemStreamException(e); }
    }
}