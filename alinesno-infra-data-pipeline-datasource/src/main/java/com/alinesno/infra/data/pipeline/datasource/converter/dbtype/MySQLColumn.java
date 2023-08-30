package com.alinesno.infra.data.pipeline.datasource.converter.dbtype;

import com.alinesno.infra.data.pipeline.datasource.converter.AbstractDbColumnType;
import com.alinesno.infra.data.pipeline.datasource.ddl.TableField;

public class MySQLColumn extends AbstractDbColumnType {

    private final static MySQLColumn mySQLColumn = new MySQLColumn();

    @Override
    public String getColumnType(TableField field) {
       StringBuilder sb = new StringBuilder()  ;

       int length = field.getLength() ;

        String f = switch (field.getFieldType()) {
            case BYTE -> "TINYINT";
            case SHORT -> "SMALLINT";
            case INT -> "INT";
            case LONG -> "BIGINT";
            case FLOAT -> "FLOAT";
            case DOUBLE -> "DOUBLE";
            case CHAR -> "CHAR(" + length + ")";
            case BOOLEAN -> "BOOLEAN";
            case STRING -> "VARCHAR(" + length + ")";
            case ARRAY -> "TEXT";
            case JSON -> "JSON";
            case MAP -> "TEXT";
            default -> "";
        };

        sb.append(f) ;
        sb.append(field.getChartSet()).append(" ");
        sb.append("COMMENT '").append(field.getComment()).append("'") ;

        return sb.toString();
    }

    public static MySQLColumn getInstance() {
        return mySQLColumn ;
    }
}
