package com.alinesno.infra.data.pipeline.scheduler.dto;

import com.alinesno.infra.data.pipeline.scheduler.enums.SourceReaderEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 读取端
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SourceReader extends BaseParams  {

    private long id ; // 唯一标识
    private String type = SourceReaderEnums.MYSQL.getCode() ;
    private String name ;

    // 查询SQL
    private String querySql ;

}
