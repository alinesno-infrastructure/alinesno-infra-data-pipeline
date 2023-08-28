package com.alinesno.infra.data.pipeline.scheduler.dto;


import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.data.pipeline.scheduler.enums.SinkReaderEnums;
import com.alinesno.infra.data.pipeline.scheduler.enums.SourceReaderEnums;
import com.alinesno.infra.data.pipeline.scheduler.reader.JdbcSourceReader;
import com.alinesno.infra.data.pipeline.scheduler.writer.JdbcSinkWriter;

import java.util.ArrayList;
import java.util.List;

class TaskInfoDtoTest {

    public static void main(String[] args) {
        testToString();
    }

    static void testToString(){
        TaskInfoDto taskInfoDto = new TaskInfoDto() ;
        taskInfoDto.setName("data_pipeline_mysql_to_clickhouse");
        taskInfoDto.setDescribe("存储服务数据迁移到数据仓库中.");

        // 上下文环境
        TaskContext taskContext = new TaskContext() ;

        taskInfoDto.setContext(taskContext);

        // 配置
        Settings settings = new Settings() ;
        settings.setCron("*/3 * * * * ?");

        taskInfoDto.setSettings(settings);

        // 读取端
        JdbcSourceReader reader = new JdbcSourceReader();
        reader.setName("mysql");
        reader.setType(SourceReaderEnums.MYSQL.getCode());
        reader.setDriverClass("com.mysql.cj.jdbc.Driver");
        reader.setJdbcUrl("jdbc:mysql://localhost:3306/dev_alinesno_infra_data_pipeline_v100?serverTimezone=GMT%2B8&zeroDateTimeBehavior=CONVERT_TO_NULL");
        reader.setPassword("adminer");
        reader.setUsername("root");
        reader.setQuerySql("select * from kfinfo");

        taskInfoDto.setReader(reader);

        // 写入端
        JdbcSinkWriter writer = new JdbcSinkWriter();
        writer.setName("clickhouse");
        writer.setType(SinkReaderEnums.CLICKHOUSE.getCode());
        writer.setDriverClass("com.mysql.cj.jdbc.Driver");
        writer.setJdbcUrl("jdbc:mysql://localhost:3306/dev_alinesno_infra_data_pipeline_v100?serverTimezone=GMT%2B8&zeroDateTimeBehavior=CONVERT_TO_NULL");
        writer.setPassword("adminer");
        writer.setUsername("root");
        writer.setWriteModel("append");

        taskInfoDto.setWriter(writer);

        // 数据过滤插件
        List<FilterPlugins> plugins = new ArrayList<>();

        FilterPlugins securityPlugin = new FilterPlugins() ;
        securityPlugin.setCode("security");
        securityPlugin.setName("安全过滤应用");

        FilterPlugins nullPlugin = new FilterPlugins() ;
        nullPlugin.setCode("null_filter");
        nullPlugin.setName("去掉为空数据");

        plugins.add(securityPlugin) ;
        plugins.add(nullPlugin) ;

        taskInfoDto.setPlugins(plugins);

        // 字段映射关系
        List<FieldMap> fileMap = new ArrayList<>() ;

        taskInfoDto.setFileMap(fileMap);

        System.out.println("taskInfoDto = \r\n " + JSONObject.toJSONString(taskInfoDto));
    }

}