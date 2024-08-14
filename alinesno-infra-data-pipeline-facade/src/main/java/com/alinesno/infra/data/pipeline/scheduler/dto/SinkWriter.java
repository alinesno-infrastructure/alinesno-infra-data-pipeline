package com.alinesno.infra.data.pipeline.scheduler.dto;

import com.alinesno.infra.data.pipeline.scheduler.enums.SourceReaderEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 写入端
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SinkWriter extends BaseParams {

    private long id ; // 唯一标识
    private String type = SourceReaderEnums.MYSQL.getCode() ;
    private String name ;

    private String writeModel ; // insert/replace

}
