package com.alinesno.infra.data.pipeline.scheduler.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 任务信息
 */
public class TaskInfoDto implements Serializable {

    // taskInfo
    private String taskType ; // 任务类型
    private String name ;  // 任务名称
    private String describe ; // 任务描述

    // 上下文环境
    private TaskContext context ;

    // 配置
    private Settings settings ;

    // 读取端
    private SourceReader reader ;

    // 写入端
    private SinkWriter writer ;

    // 数据过滤插件
    private List<FilterPlugins> plugins ;

    // 字段映射关系
    private List<FieldMap> fileMap ;

    public TaskInfoDto() {
    }

    public TaskInfoDto(String taskType) {
        this.taskType = taskType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public TaskContext getContext() {
        return context;
    }

    public void setContext(TaskContext context) {
        this.context = context;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public SourceReader getReader() {
        return reader;
    }

    public void setReader(SourceReader reader) {
        this.reader = reader;
    }

    public SinkWriter getWriter() {
        return writer;
    }

    public void setWriter(SinkWriter writer) {
        this.writer = writer;
    }

    public List<FilterPlugins> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<FilterPlugins> plugins) {
        this.plugins = plugins;
    }

    public List<FieldMap> getFileMap() {
        return fileMap;
    }

    public void setFileMap(List<FieldMap> fileMap) {
        this.fileMap = fileMap;
    }
}
