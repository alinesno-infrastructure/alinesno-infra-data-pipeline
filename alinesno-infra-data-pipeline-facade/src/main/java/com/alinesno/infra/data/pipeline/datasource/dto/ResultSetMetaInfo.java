package com.alinesno.infra.data.pipeline.datasource.dto;

import org.springframework.jdbc.support.JdbcUtils;
 
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetMetaInfo {

    private int columnCount;
    private int[] columnTypes;
    private String[] columnNames;
 
    public ResultSetMetaInfo(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        this.columnCount = rsmd.getColumnCount();
        this.columnTypes = new int[columnCount];
        this.columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnTypes[i] = rsmd.getColumnType(i + 1);
            columnNames[i] = JdbcUtils.lookupColumnName(rsmd, i + 1);
        }
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int[] getColumnTypes() {
        return columnTypes;
    }

    public void setColumnTypes(int[] columnTypes) {
        this.columnTypes = columnTypes;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }
}