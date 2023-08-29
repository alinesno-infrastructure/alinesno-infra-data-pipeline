package com.alinesno.infra.data.pipeline.enums;

/**
 * 字段映射值类型
 */
public enum DataMappingEnums {

    COPY("copy" , "镜像文件") ,
    SUM("sum" , "字段求和") ,
    ROW_NUMBER("row_number" , "增加序列号") ,
    DICT("dict" , "字典转换") ,
    FILTER("filter" , "过滤字段") ,
    CONST("constant" , "增加常量")
    ;

    private String code ;
    private String label ;

    private DataMappingEnums(String code , String label){
        this.code = code ;
        this.label = label ;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
