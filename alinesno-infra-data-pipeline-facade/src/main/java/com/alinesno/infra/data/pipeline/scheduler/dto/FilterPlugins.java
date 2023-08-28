package com.alinesno.infra.data.pipeline.scheduler.dto;

import com.alibaba.fastjson.JSONObject;

public class FilterPlugins {

    private String name ;
    private String code ; // 插件代码
    private JSONObject contextJson ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public JSONObject getContextJson() {
        return contextJson;
    }

    public void setContextJson(JSONObject contextJson) {
        this.contextJson = contextJson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
