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

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOssPath() {
        return ossPath;
    }

    public void setOssPath(String ossPath) {
        this.ossPath = ossPath;
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBootstraps() {
        return bootstraps;
    }

    public void setBootstraps(String bootstraps) {
        this.bootstraps = bootstraps;
    }

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
