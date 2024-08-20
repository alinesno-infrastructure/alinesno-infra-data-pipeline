package com.alinesno.infra.data.pipeline.enums;

import lombok.Getter;

@Getter
public enum TransTypeEnums {

    READER("reader") ,
    PLUGIN("plugin") ,
    WRITER("writer") ;

    public final String code ;

    private TransTypeEnums(String code) {
        this.code = code ;
    }

}
