package com.alinesno.infra.data.pipeline.scheduler.dto;

import lombok.Data;

import java.util.Date;

/**
 * 配置信息
 */
@Data
public class Settings {

    private String cron ;
    private Date startTime ;
    private Date endTime ;

}
