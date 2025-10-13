package com.example.pipeline.io.mysql;

import com.alinesno.infra.data.pipeline.demo.base.AbstractDataSinker;
import com.alinesno.infra.data.pipeline.demo.config.SinkConfig;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class MySQLDataSinker extends AbstractDataSinker<Map<String,Object>> {
    private final SinkConfig config;
    private final JdbcTemplate jdbcTemplate;
    private final int batchSize;

    public MySQLDataSinker(SinkConfig config, JdbcTemplate jdbcTemplate, int batchSize) {
        super(config);
        this.config = config;
        this.jdbcTemplate = jdbcTemplate;
        this.batchSize = batchSize;
    }

    @Override
    public void init() { /* optional */ }

    @Override
    public void batchWrite(List<Map<String, Object>> batch) {
        if (batch == null || batch.isEmpty()) return;
        Map<String,Object> first = batch.get(0);
        List<String> cols = new ArrayList<>(first.keySet());
        String colList = cols.stream().collect(Collectors.joining(","));
        String ph = cols.stream().map(c -> "?").collect(Collectors.joining(","));
        String sql = "INSERT INTO " + config.getTable() + " (" + colList + ") VALUES (" + ph + ")";
        if (config.isUpsert()) {
            String update = cols.stream().map(c -> c + "=VALUES(" + c + ")").collect(Collectors.joining(","));
            sql += " ON DUPLICATE KEY UPDATE " + update;
        }

        List<Object[]> args = new ArrayList<>();
        for (Map<String,Object> row : batch) {
            Object[] vals = cols.stream().map(row::get).toArray();
            args.add(vals);
        }
        jdbcTemplate.batchUpdate(sql, args);
    }

    @Override
    public void commit() { /* JdbcTemplate default autocommit */ }

    @Override
    public void close() { /* noop */ }
}