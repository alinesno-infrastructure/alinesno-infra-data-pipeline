package com.alinesno.infra.data.pipeline.datasource.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * DDLConverter类用于将Oracle表的DDL转换为ClickHouse的DDL，并自动创建ClickHouse表。
 */
public class OracleToClickhouseConverter extends AbstractDDLConverter {

    private static final Logger log = LoggerFactory.getLogger(OracleToClickhouseConverter.class) ;

//    /**
//     * 程序入口点，用于演示DDL转换和创建ClickHouse表。
//     */
//    public static void main(String[] args) {
//        String oracleSchema = "your_oracle_schema";
//        String oracleTableName = "your_oracle_table_name";
//        convertDDL(oracleSchema, oracleTableName);
//    }

    /**
     * 将Oracle表的DDL转换为ClickHouse的DDL，并自动创建ClickHouse表。
     *
     * @param oracleSchema     Oracle表所在的模式或用户
     * @param oracleTableName  Oracle表的名称
     */
    public static void convertDDL(String oracleSchema, String oracleTableName , Connection oracleConnection , Connection clickhouseConnection ) {
        try {
            // 检索Oracle表结构的DDL
            String ddl = retrieveDDL(oracleConnection, oracleSchema, oracleTableName);

            // 创建ClickHouse表
            createTable(clickhouseConnection, oracleTableName, ddl);

            // 关闭连接
            oracleConnection.close();
            clickhouseConnection.close();

            System.out.println("DDL转换成功。");
        } catch (SQLException e) {
            log.error("DDL转换异常:{}" , e.getMessage());
        }
    }

    /**
     * 检索Oracle表结构的DDL。
     *
     * @param connection      Oracle数据库连接
     * @param schema          Oracle表所在的模式或用户
     * @param tableName       Oracle表的名称
     * @return                ClickHouse表的DDL
     * @throws SQLException  如果检索DDL时发生错误
     */
    public static String retrieveDDL(Connection connection, String schema, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getColumns(null, schema.toUpperCase(), tableName.toUpperCase(), null);

        StringBuilder ddlBuilder = new StringBuilder();
        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            String columnType = resultSet.getString("TYPE_NAME");
            int columnSize = resultSet.getInt("COLUMN_SIZE");
            String nullable = resultSet.getString("IS_NULLABLE");
            String defaultValue = resultSet.getString("COLUMN_DEF");

            ddlBuilder.append(columnName).append(" ").append(mapColumnType(columnType, columnSize));

            if (nullable.equalsIgnoreCase("NO")) {
                ddlBuilder.append(" NOT NULL");
            }

            if (defaultValue != null) {
                ddlBuilder.append(" DEFAULT ").append(defaultValue);
            }

            ddlBuilder.append(", ");
        }

        String ddl = ddlBuilder.toString();
        ddl = ddl.substring(0, ddl.length() - 2); // 去除最后的逗号和空格

        return ddl;
    }

    /**
     * 将Oracle数据类型映射为ClickHouse数据类型。
     *
     * @param columnType  Oracle数据类型
     * @param columnSize  列大小
     * @return            ClickHouse数据类型
     */
    public static String mapColumnType(String columnType, int columnSize) {
        // 添加你自己的数据类型映射逻辑，将Oracle数据类型转换为ClickHouse数据类型
        // 这里提供了一些常见的映射示例，你可以根据需要进行修改
        if (columnType.equalsIgnoreCase("VARCHAR2")) {
            return "String";
        } else if (columnType.equalsIgnoreCase("NUMBER")) {
            if (columnSize == 1) {
                return "UInt8";
            } else if (columnSize <= 5) {
                return "Int16";
            } else if (columnSize <= 10) {
                return "Int32";
            } else if (columnSize <= 19) {
                return "Int64";
            } else {
                return "Decimal(18, 2)";
            }
        } else if (columnType.equalsIgnoreCase("DATE")) {
            return "Date";
        } else {
            return columnType;
        }
    }

    /**
     * 在ClickHouse中创建表。
     *
     * @param connection      ClickHouse数据库连接
     * @param tableName       ClickHouse表的名称
     * @param ddl             ClickHouse表的DDL
     * @throws SQLException  如果创建表时发生错误
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