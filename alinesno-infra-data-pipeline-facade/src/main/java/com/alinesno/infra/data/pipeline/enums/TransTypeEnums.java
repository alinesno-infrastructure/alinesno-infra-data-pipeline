package com.alinesno.infra.data.pipeline.enums;

public enum TransTypeEnums {

    READER("reader") , PLUGIN("plugin") , WRITER("writer") ;

    public final String code ;

    private TransTypeEnums(String code) {
        this.code = code ;
    }

    public String getCode() {
        return code;
    }
}
