package com.alinesno.infra.data.pipeline.datasource.converter.dbtype;

import com.alinesno.infra.data.pipeline.datasource.converter.AbstractDbColumnType;
import com.alinesno.infra.data.pipeline.datasource.ddl.TableField;
import com.alinesno.infra.data.pipeline.datasource.enums.JavaType;

public class OracleColumn extends AbstractDbColumnType {

    private final static OracleColumn oracleColumn = new OracleColumn();

    @Override
    public String getColumnType(TableField field) {
        StringBuilder sb = new StringBuilder()  ;

        JavaType fieldType = field.getFieldType() ;
        int length = field.getLength() ;

        String f = switch (fieldType) {
            case BYTE -> "NUMBER(3)";
            case SHORT -> "NUMBER(5)";
            case INT -> "NUMBER(10)";
            case LONG -> "NUMBER(19)";
            case FLOAT -> "BINARY_FLOAT";
            case DOUBLE -> "BINARY_DOUBLE";
            case CHAR -> "CHAR(" + length + ")";
            case BOOLEAN -> "NUMBER(1)";
            case STRING -> "VARCHAR2(" + length + ")";
            case ARRAY -> "CLOB";
            case JSON -> "CLOB";
            case MAP -> "CLOB";
            default -> "";
        };

        sb.append(f) ;
        sb.append(field.getChartSet()).append(" ");
        sb.append("COMMENT '").append(field.getComment()).append("',\n");

        return sb.toString();
    }

    public static OracleColumn getInstance() {
        return oracleColumn ;
    }
}
