package com.alinesno.infra.data.pipeline.scheduler.dto;

import com.alinesno.infra.data.pipeline.scheduler.enums.SourceReaderEnums;

/**
 * 读取端
 */
public class SourceReader extends BaseParams  {

    private String id ; // 唯一标识
    private String type = SourceReaderEnums.MYSQL.getCode() ;
    private String name ;

    // 查询SQL
    private String querySql ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
}
