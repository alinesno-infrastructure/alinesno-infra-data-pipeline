package com.alinesno.infra.data.pipeline.scheduler.dto;

import com.alinesno.infra.data.pipeline.scheduler.enums.SourceReaderEnums;

/**
 * 写入端
 */
public class SinkWriter {

    private String id ; // 唯一标识
    private String type = SourceReaderEnums.MYSQL.getCode() ;
    private String name ;

    private String writeModel ; // insert/replace

    // jdbc info
    private String jdbcUrl ;
    private String driverClass ;
    private String username ;
    private String password ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getWriteModel() {
        return writeModel;
    }

    public void setWriteModel(String writeModel) {
        this.writeModel = writeModel;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
