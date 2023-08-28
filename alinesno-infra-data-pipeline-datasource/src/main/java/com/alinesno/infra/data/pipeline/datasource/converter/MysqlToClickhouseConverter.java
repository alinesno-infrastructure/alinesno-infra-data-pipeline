package com.alinesno.infra.data.pipeline.datasource.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类实现了将MySQL表的DDL转换为ClickHouse的DDL。
 */
public class MysqlToClickhouseConverter extends AbstractDDLConverter {

    private static final Logger log = LoggerFactory.getLogger(MysqlToClickhouseConverter.class) ;

    /**
     * 将MySQL表的DDL转换为ClickHouse的DDL。
     *
     * @param tableName 要转换的表名
     */
    public static void convertDDL(String tableName , Connection mysqlConnection , Connection clickhouseConnection) {
        try {
            // 检索MySQL表结构的DDL
            String ddl = retrieveDDL(mysqlConnection, tableName);

            // 创建ClickHouse表
            createTable(clickhouseConnection, tableName, ddl);

            // 关闭连接
            mysqlConnection.close();
            clickhouseConnection.close();

            System.out.println("DDL转换成功。");
        } catch (SQLException e) {
            log.error("DDL转换异常:{}" , e.getMessage());
        }
    }

    /**
     * 从MySQL数据库中检索表结构的DDL。
     *
     * @param connection MySQL数据库连接
     * @param tableName  表名
     * @return 表的DDL字符串
     * @throws SQLException 如果发生数据库访问错误
     */
    public static String retrieveDDL(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getColumns(null, null, tableName, null);

        List<String> columnDefs = new ArrayList<>();
        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            String columnType = resultSet.getString("TYPE_NAME");
            int columnSize = resultSet.getInt("COLUMN_SIZE");
            String columnDef = columnName + " " + mapColumnType(columnType, columnSize);
            columnDefs.add(columnDef);
        }

        return String.join(", ", columnDefs);
    }

    /**
     * 将MySQL数据类型映射为ClickHouse数据类型。
     *
     * @param columnType MySQL数据类型
     * @param columnSize 列大小
     * @return ClickHouse数据类型
     */
    public static String mapColumnType(String columnType, int columnSize) {
        // 添加你自己的数据类型映射逻辑，将MySQL数据类型转换为ClickHouse数据类型
        // 这里提供了一些常见的映射示例，你可以根据需要进行修改
        if (columnType.equalsIgnoreCase("VARCHAR")) {
            return "String";
        } else if (columnType.equalsIgnoreCase("INT")) {
            return "Int32";
        } else if (columnType.equalsIgnoreCase("BIGINT")) {
            return "Int64";
        } else if (columnType.equalsIgnoreCase("DECIMAL")) {
            return "Decimal(18, 2)";
        } else {
            return columnType;
        }
    }

    /**
     * 在ClickHouse中创建表。
     *
     * @param connection ClickHouse数据库连接
     * @param tableName  表名
     * @param ddl        表的DDL字符串
     * @throws SQLException 如果发生数据库访问错误
     */
    public static void createTable(Connection connection, String tableName, String ddl) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + ddl + ")";
            statement.executeUpdate(createTableQuery);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
}
