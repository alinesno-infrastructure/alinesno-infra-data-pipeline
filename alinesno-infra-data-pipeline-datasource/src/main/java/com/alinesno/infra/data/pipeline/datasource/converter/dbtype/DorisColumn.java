package com.alinesno.infra.data.pipeline.datasource.converter.dbtype;

import com.alinesno.infra.data.pipeline.datasource.converter.AbstractDbColumnType;
import com.alinesno.infra.data.pipeline.datasource.ddl.TableField;
import com.alinesno.infra.data.pipeline.datasource.enums.JavaType;

public class DorisColumn extends AbstractDbColumnType {

    private final static DorisColumn dorisColumn = new DorisColumn();

    @Override
    public String getColumnType(TableField field) {
        StringBuilder sb = new StringBuilder()  ;

        JavaType fieldType = field.getFieldType() ;
        int length = field.getLength() ;

        String f = switch (fieldType) {
            case BYTE -> "Byte";
            case SHORT -> "Short";
            case INT -> "Int";
            case LONG -> "Long";
            case FLOAT -> "Float";
            case DOUBLE -> "Double";
            case CHAR -> "Char";
            case BOOLEAN -> "Boolean";
            case STRING -> "String";
            case ARRAY -> "Array";
            case JSON -> "Json";
            case MAP -> "Map";
            default -> "";
        };

        sb.append(f) ;
        sb.append(field.getChartSet()).append(" ");
        sb.append("COMMENT '").append(field.getComment()).append("',\n");

        return sb.toString();
    }

    public static DorisColumn getInstance() {
        return dorisColumn ;
    }

}
