package com.alinesno.infra.data.pipeline.scheduler.enums;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 任务类型枚举类
 * 用于表示不同的任务类型
 */
@Getter
public enum TaskTypeEnums {

    RECURRING_TASK("recurring_task"), // 循环任务
    ONE_TIME_TASK("one_time_task"), // 一次性任务
    LONG_RUNNING_TASK("long_running_task"), // 长时间运行任务
    CRON_TASK("cron_task"); // Cron表达式任务

    private final String code;

    /**
     * 构造函数
     * @param code 任务类型代码值
     */
    TaskTypeEnums(String code) {
        this.code = code;
    }

    /**
     * 获取任务类型代码值
     * @return 任务类型代码值
     */
    public String getCode() {
        return code;
    }
}