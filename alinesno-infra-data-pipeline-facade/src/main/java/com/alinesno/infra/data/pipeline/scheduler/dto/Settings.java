package com.alinesno.infra.data.pipeline.scheduler.dto;

import java.util.Date;

/**
 * 配置信息
 */
public class Settings {

    private String cron ;
    private Date startTime ;
    private Date endTime ;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
