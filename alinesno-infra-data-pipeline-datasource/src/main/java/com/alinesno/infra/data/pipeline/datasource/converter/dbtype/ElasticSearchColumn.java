package com.alinesno.infra.data.pipeline.datasource.converter.dbtype;

import com.alinesno.infra.data.pipeline.datasource.converter.AbstractDbColumnType;
import com.alinesno.infra.data.pipeline.datasource.ddl.TableField;
import com.alinesno.infra.data.pipeline.datasource.enums.JavaType;

public class ElasticSearchColumn extends AbstractDbColumnType {

    private final static ElasticSearchColumn elasticSearchColumn = new ElasticSearchColumn();

    @Override
    public String getColumnType(TableField field) {
        StringBuilder sb = new StringBuilder()  ;

        JavaType fieldType = field.getFieldType() ;
        int length = field.getLength() ;

        String f = switch (fieldType) {
            case BYTE -> "byte";
            case SHORT -> "short";
            case INT -> "integer";
            case LONG -> "long";
            case FLOAT -> "float";
            case DOUBLE -> "double";
            case CHAR -> "keyword";
            case BOOLEAN -> "boolean";
            case STRING -> "text";
            case ARRAY -> "nested";
            case JSON -> "text";
            case MAP -> "nested";
            default -> "";
        };

        sb.append(f) ;
        sb.append(field.getChartSet()).append(" ");
        sb.append("COMMENT '").append(field.getComment()).append("',\n");

        return sb.toString();
    }

    public static ElasticSearchColumn getInstance() {
        return elasticSearchColumn ;
    }
}
