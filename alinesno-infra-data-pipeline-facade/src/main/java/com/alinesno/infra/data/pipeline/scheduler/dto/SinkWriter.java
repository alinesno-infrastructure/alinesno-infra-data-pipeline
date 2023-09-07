package com.alinesno.infra.data.pipeline.scheduler.dto;

import com.alinesno.infra.data.pipeline.scheduler.enums.SourceReaderEnums;

/**
 * 写入端
 */
public class SinkWriter extends BaseParams {

    private String id ; // 唯一标识
    private String type = SourceReaderEnums.MYSQL.getCode() ;
    private String name ;

    private String writeModel ; // insert/replace

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

    public String getWriteModel() {
        return writeModel;
    }

    public void setWriteModel(String writeModel) {
        this.writeModel = writeModel;
    }
}
