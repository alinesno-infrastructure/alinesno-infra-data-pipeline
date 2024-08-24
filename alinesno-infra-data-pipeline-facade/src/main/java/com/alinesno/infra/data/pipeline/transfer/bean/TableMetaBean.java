package com.alinesno.infra.data.pipeline.transfer.bean;

import lombok.Data;

/**
 * 字段信息Bean
 */
@Data
public class TableMetaBean {

    private String name; // 表名称
    private String comment; // 表注释
    private String engine; // 数据库引擎
    private String dataSize ; // 数据量大小

}
