package com.alinesno.infra.data.pipeline.datasource.ddl;

import com.alinesno.infra.data.pipeline.datasource.enums.JavaType;
import org.apache.commons.lang.StringUtils;

/**
 * 数据库字段
 */
public class TableField {

    private String name;
    private String desc;
    private JavaType fieldType;
    private int length;
    private String comment;
    private String chartSet = "";
    private boolean isNull ;
    private boolean isPrimaryKey ;

    public TableField(String name, String desc, JavaType fieldType, int length) {
        this.name = name;
        this.desc = desc;
        this.fieldType = fieldType;
        this.length = length;
        this.comment = desc ;
    }

    public TableField(String name, String desc, JavaType fieldType, int length, String comment, String chartSet) {
        this.name = name;
        this.desc = desc;
        this.fieldType = fieldType;
        this.length = length;

        if(StringUtils.isEmpty(comment)){
           this.comment = desc ;
        }else{
            this.comment = comment;
        }

        this.chartSet = chartSet;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public TableField setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
        return this ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setFieldType(JavaType fieldType) {
        this.fieldType = fieldType;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setChartSet(String chartSet) {
        this.chartSet = chartSet;
    }

    public String getName() {
        return name;
    }

    public JavaType getFieldType() {
        return fieldType;
    }

    public int getLength() {
        return length;
    }

    public String getComment() {
        return comment;
    }

    public String getChartSet() {
        return chartSet;
    }
}
