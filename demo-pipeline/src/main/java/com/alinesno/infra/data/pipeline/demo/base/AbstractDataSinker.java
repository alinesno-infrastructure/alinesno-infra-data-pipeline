package com.alinesno.infra.data.pipeline.demo.base;

import java.io.Closeable;
import java.util.List;

public abstract class AbstractDataSinker<T> implements Closeable {
    public final Object config;
    public AbstractDataSinker(Object config) { this.config = config; }
    public abstract void init() throws Exception;
    public abstract void batchWrite(List<T> batch) throws Exception;
    public abstract void commit() throws Exception;
}