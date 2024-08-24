package com.alinesno.infra.data.pipeline.api.utils;

import com.alinesno.infra.data.pipeline.api.FetchDataDto;
import com.alinesno.infra.data.pipeline.api.dto.DatasourceDto;
import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import com.alinesno.infra.data.pipeline.enums.DbDriverEnums;
import com.alinesno.infra.data.pipeline.scheduler.dto.SourceReader;
import org.springframework.beans.BeanUtils;

/**
 * ReaderSourceMapping类用于根据资源类型生成相应的数据源读取器
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
public class ReaderSourceMapping {

    /**
     * 根据数据源实体和请求数据生成SourceReader对象
     *
     * @param resourceSource 数据源实体，包含数据源的配置信息
     * @param dto 请求数据传输对象，可能包含特定的查询信息
     * @return 根据数据源类型生成的SourceReader对象
     */
    public static SourceReader getReaderSource(ReaderSourceEntity resourceSource, FetchDataDto dto) {
        SourceReader sourceReader = new SourceReader();
        String type = resourceSource.getReaderType();

        // 根据数据源类型返回相应的SourceReader，支持mysql和oracle，其他类型则返回默认的SourceReader
        return switch (type) {
            case "mysql", "oracle" -> genDatabaseSource(type, resourceSource, dto);
            default -> sourceReader;
        };
    }

    /**
     * 生成数据库数据源的SourceReader对象
     *
     * @param type 数据库类型，如mysql、oracle
     * @param resourceSource 数据源实体，包含数据库的连接信息
     * @param dto 请求数据传输对象，可能包含SQL查询语句
     * @return 配置了数据库连接信息的SourceReader对象
     */
    private static SourceReader genDatabaseSource(String type, ReaderSourceEntity resourceSource, FetchDataDto dto) {
        SourceReader reader = new SourceReader();

        reader.setJdbcUrl(resourceSource.getReaderUrl());
        reader.setDriverClass(DbDriverEnums.getDbDriver(type));
        reader.setUsername(resourceSource.getReaderUsername());
        reader.setPassword(resourceSource.getReaderPasswd());

        // 如果FetchDataDto不为空，则设置SQL查询语句
        if(dto != null){
            reader.setQuerySql(dto.getSql());
        }

        return reader;
    }

    /**
     * 通过DatasourceDto生成SourceReader对象，适用于DTO中携带必要信息的场景
     *
     * @param dto 数据源传输对象，包含数据源的配置信息
     * @return 根据数据源信息生成的SourceReader对象
     */
    public static SourceReader getReaderSourceByDto(DatasourceDto dto) {
        ReaderSourceEntity resourceSource = new ReaderSourceEntity();
        BeanUtils.copyProperties(dto, resourceSource);

        // 调用getReaderSource方法，传入空的FetchDataDto，不指定特定查询
        return ReaderSourceMapping.getReaderSource(resourceSource, null);
    }
}
