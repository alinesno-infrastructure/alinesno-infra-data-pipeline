package com.alinesno.infra.data.pipeline.datasource;

import com.alinesno.infra.common.core.cache.RedisUtils;
import com.alinesno.infra.data.pipeline.constants.PipeConstants;
import com.alinesno.infra.data.pipeline.scheduler.dto.SourceReader;
import com.alinesno.infra.data.pipeline.scheduler.enums.SinkReaderEnums;
import com.alinesno.infra.data.pipeline.transfer.bean.FieldMetaBean;
import com.alinesno.infra.data.pipeline.transfer.bean.TableMetaBean;
import com.google.common.base.CharMatcher;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class ComponentSourceReader extends AbstractTemplates implements IDataSourceReader {

    protected String buildQuerySql(SourceReader reader) {
        return reader.getQuerySql() ;
    }

    /**
     * 大文件：处理单行
     */
    public void processRow(int columnCount, ResultSet rs, StringBuilder sb) throws SQLException {
        for (int i = 1; i <= columnCount; i++) {
            Object value = JdbcUtils.getResultSetValue(rs, i);
            if(value != null){
                String result = CharMatcher.breakingWhitespace().removeFrom(value.toString()) ;
                sb.append(result).append(",");
            }else{
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1).append("\n");
    }

    @SneakyThrows
    @Override
    public List<FieldMetaBean> analyseSourceField(SourceReader source) {

        List<FieldMetaBean> fieldMetas = new ArrayList<>();

        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = getDataSource(source);
            String sql = buildQuerySql(source);
            rs = connection.createStatement().executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData() ;
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                FieldMetaBean fieldMeta = new FieldMetaBean();

                // 获取到注释信息
                fieldMeta.setName(metaData.getColumnName(i));
                fieldMeta.setType(metaData.getColumnTypeName(i));

                fieldMetas.add(fieldMeta);
            }
        } finally {
            closeQuietly(rs);
            closeQuietly(connection);
        }

        return fieldMetas;
    }

    @SneakyThrows
    @Override
    public List<Map<String, Object>> fetchData(SourceReader source) {
        List<Map<String, Object>> rows = new ArrayList<>();

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            connection = getDataSource(source);
            String sql = buildQuerySql(source);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                rows.add(row);
            }
        } finally {
            closeQuietly(rs);
            closeQuietly(stmt);
            closeQuietly(connection);
        }

        return rows;
    }

    /**
     * 获取数据源表结构
     * @param reader
     * @return
     */
    @SneakyThrows
    @Override
    public List<TableMetaBean> fetchTableData(SourceReader reader){
        List<TableMetaBean> tableMetaBeans = new ArrayList<>();

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = getDataSource(reader);

            String dbName = getCurrentDatabaseNameFromURL(reader.getJdbcUrl()) ;
            log.debug("dbName = {}" , dbName);

            // SQL 查询语句 - 获取所有表的名称
            String sql = "SELECT TABLE_NAME, TABLE_COMMENT FROM information_schema.tables WHERE TABLE_SCHEMA = ?";

            // 准备 SQL 语句
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, dbName); // 替换为实际的数据库名

            // 执行查询
            rs = stmt.executeQuery();

            // 处理结果集
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String comment = rs.getString("TABLE_COMMENT");
                int rowCount = getRowCount(connection, tableName);

                log.debug("Table Name: " + tableName + ", Comment: " + comment + ", Row Count: " + rowCount);

                TableMetaBean tableMetaBean = new TableMetaBean();
                tableMetaBean.setComment(comment);
                tableMetaBean.setDataSize(rowCount);
                tableMetaBean.setEngine("");
                tableMetaBean.setName(tableName);

                tableMetaBeans.add(tableMetaBean) ;
            }
        } finally {
            // 关闭资源
            closeQuietly(rs);
            closeQuietly(stmt);
            closeQuietly(connection);
        }

        return tableMetaBeans;
    }

    private static String getCurrentDatabaseNameFromURL(String url) {
        int index = url.indexOf('?');
        if (index == -1) {
            index = url.length();
        }
        String databasePart = url.substring(url.indexOf("//") + 2, index);
        int lastSlashIndex = databasePart.lastIndexOf('/');
        if (lastSlashIndex != -1) {
            return databasePart.substring(lastSlashIndex + 1);
        }
        return null;
    }

    private static int getRowCount(Connection conn, String tableName) throws SQLException {
        String countSql = "SELECT COUNT(*) AS count FROM " + tableName;
        PreparedStatement countPstmt = conn.prepareStatement(countSql);
        ResultSet countRs = countPstmt.executeQuery();
        int rowCount = 0;
        if (countRs.next()) {
            rowCount = countRs.getInt("count");
        }
        countRs.close();
        countPstmt.close();
        return rowCount;
    }

    @Override
    public void checkConnection(SourceReader reader) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // 获取数据源连接
            connection = getDataSource(reader);

            // 创建 Statement
            stmt = connection.createStatement();

            // 查询所有表的元数据
            rs = stmt.executeQuery(SinkReaderEnums.getValidateQuery(reader.getType()));
            rs.next();
        } catch (Exception e){
            throw new RuntimeException("数据源连接失败！" + e.getMessage()) ;
        }finally {
            // 关闭资源
            closeQuietly(rs);
            closeQuietly(stmt);
            closeQuietly(connection);
        }
    }


    /**
     * 估算数据量大小
     * @param connection
     * @param tableName
     * @return
     * @throws Exception
     */
    @SneakyThrows
    private int estimateDataSize(Connection connection, String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0 ;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);

            if(rs.next()){
                count = rs.getInt(1);
            }
        }finally {
            closeQuietly(rs);
            closeQuietly(stmt);
        }
        return count;
    }

    /**
     * 关闭资源
     * @param resource
     */
    private void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Ignore exceptions on close
            }
        }
    }

}
