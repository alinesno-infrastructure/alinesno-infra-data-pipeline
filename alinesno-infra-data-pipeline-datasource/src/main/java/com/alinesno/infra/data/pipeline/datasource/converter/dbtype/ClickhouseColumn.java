package com.alinesno.infra.data.pipeline.datasource.converter.dbtype;

import com.alinesno.infra.data.pipeline.datasource.converter.AbstractDbColumnType;
import com.alinesno.infra.data.pipeline.datasource.ddl.TableField;
import com.alinesno.infra.data.pipeline.datasource.enums.JavaType;

public class ClickhouseColumn extends AbstractDbColumnType {

    private final static ClickhouseColumn clickhouseColumn = new ClickhouseColumn();

    @Override
    public String getColumnType(TableField field) {
        StringBuilder sb = new StringBuilder()  ;

        JavaType fieldType = field.getFieldType() ;
        int length = field.getLength() ;

        String f =  switch (fieldType) {
            case BYTE -> "Int8";
            case SHORT -> "Int16";
            case INT -> "Int32";
            case LONG -> "Int64";
            case FLOAT -> "Float32";
            case DOUBLE -> "Float64";
            case CHAR -> "FixedString(" + length + ")";
            case BOOLEAN -> "UInt8";
            case STRING -> "String";
            case ARRAY -> "Array(String)";
            case JSON -> "String";
            case MAP -> "String";
            default -> "";
        };

        sb.append(f) ;
        sb.append(field.getChartSet()).append(" ");
        sb.append("COMMENT '").append(field.getComment()).append("'") ;

        return sb.toString();
    }

    public static ClickhouseColumn getInstance() {
        return clickhouseColumn ;
    }

}
