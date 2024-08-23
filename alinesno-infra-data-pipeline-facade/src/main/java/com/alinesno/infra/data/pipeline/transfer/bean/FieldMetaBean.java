package com.alinesno.infra.data.pipeline.transfer.bean;

import lombok.Data;

/**
 * 字段信息Bean
 */
@Data
public class FieldMetaBean {

    private String name; // 字段名称
    private String type; // 字段类型
    private String comment; // 字段注释
    private String defaultValue;  // 默认值
    private String nullable; // 是否允许为空
    private String primaryKey; // 是否为主键
    private String autoIncrement; // 是否自增
    private String unique; // 是否唯一

}
