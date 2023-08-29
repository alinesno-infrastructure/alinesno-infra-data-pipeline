package com.alinesno.infra.data.pipeline.datasource;

import com.alinesno.infra.data.pipeline.enums.DataMappingEnums;

import java.util.Map;

public class MappingBean {

    private String sourceField;
    private String targetField;
    private String mappingPlugin = DataMappingEnums.COPY.getCode(); // 映射关系插件
    private Map<String, String> dict;

    public MappingBean() {
    }

    public MappingBean(String sourceField, String targetField) {
        this.sourceField = sourceField;
        this.targetField = targetField;
    }

    public MappingBean(String sourceField, String targetField, String mappingPlugin) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.mappingPlugin = mappingPlugin;
    }

    public MappingBean(String sourceField, String targetField, String mappingPlugin, Map<String, String> dictMap) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.mappingPlugin = mappingPlugin;
        this.dict = dictMap;
    }

    public Map<String, String> getDict() {
        return dict;
    }

    public void setDict(Map<String, String> dict) {
        this.dict = dict;
    }

    public String getMappingPlugin() {
        return mappingPlugin;
    }

    public void setMappingPlugin(String mappingPlugin) {
        this.mappingPlugin = mappingPlugin;
    }

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }
}