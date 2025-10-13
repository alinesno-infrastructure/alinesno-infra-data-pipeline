package com.alinesno.infra.data.pipeline.demo.base;

import java.io.Closeable;
import java.util.Iterator;

public abstract class AbstractDataReader<T> implements Closeable {
    public final Object config;
    public AbstractDataReader(Object config) { this.config = config; }
    public abstract void init() throws Exception;
    public abstract Iterator<T> read() throws Exception;
}