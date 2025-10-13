package com.example.pipeline.io.mysql;

import com.alinesno.infra.data.pipeline.demo.base.AbstractDataReader;
import com.alinesno.infra.data.pipeline.demo.config.DataSourceConfig;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSetMetaData;
import java.util.*;

public class MySQLDataReader extends AbstractDataReader<Map<String,Object>> {
    private final DataSourceConfig config;
    private final JdbcTemplate jdbcTemplate;
    private List<Map<String,Object>> rows;

    public MySQLDataReader(DataSourceConfig config, JdbcTemplate jdbcTemplate) {
        super(config);
        this.config = config;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void init() { /* nothing */ }

    @Override
    public Iterator<Map<String, Object>> read() {
        rows = jdbcTemplate.query(config.getSql(), (rs, rowNum) -> {
            ResultSetMetaData md = rs.getMetaData();
            Map<String,Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                row.put(md.getColumnLabel(i), rs.getObject(i));
            }
            return row;
        });
        return rows.iterator();
    }

    @Override
    public void close() { /* nothing */ }
}