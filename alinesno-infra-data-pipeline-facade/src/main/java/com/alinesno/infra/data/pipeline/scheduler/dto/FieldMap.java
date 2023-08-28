package com.alinesno.infra.data.pipeline.scheduler.dto;

/**
 * 字段映射关系
 */
public class FieldMap {

    private String sourceField ;
    private String sinkField ;

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getSinkField() {
        return sinkField;
    }

    public void setSinkField(String sinkField) {
        this.sinkField = sinkField;
    }
}
