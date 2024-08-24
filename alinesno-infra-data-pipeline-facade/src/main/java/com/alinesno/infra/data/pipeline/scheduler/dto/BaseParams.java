package com.alinesno.infra.data.pipeline.scheduler.dto;

import lombok.Data;

@Data
public abstract class BaseParams {

    // elasticsearch
    private String esHost ;
    private String esPort ;

    // jdbc info
    private String tableName ;
    private String jdbcUrl ;
    private String driverClass ;
    private String username ;
    private String password ;

    // download
    private String downloadUrl ;
    private String filename ;

    // kafka info
    private String topic ;
    private String bootstraps ;

    // minio
    private String endPoint ;
    private String accessKey;
    private String securityKey ;
    private String bucket ;
    private String ossPath ;

    // FTP
    private String ftpHost ;
    private int ftpPort ;
    private String ftpPath ;

}
