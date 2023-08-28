package com.alinesno.infra.data.pipeline.enums;

/**
 * 任务状态
 */
public enum JobStatusEnums {
    /**
     * 表示任务处于待处理状态
     */
    PENDING("pending", "待处理"),

    /**
     * 表示任务处于处理中状态
     */
    PROCESSING("processing", "处理中"),

    /**
     * 表示任务已完成
     */
    COMPLETED("completed", "已完成"),

    /**
     * 表示任务失败
     */
    FAILED("failed", "失败");

    private final String status;
    private final String label;

    JobStatusEnums(String status, String label) {
        this.status = status;
        this.label = label;
    }

    public String getStatus() {
        return status;
    }

    public String getLabel() {
        return label;
    }
}
