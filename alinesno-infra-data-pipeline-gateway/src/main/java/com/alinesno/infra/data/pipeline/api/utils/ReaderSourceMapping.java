package com.alinesno.infra.data.pipeline.api.utils;

import com.alinesno.infra.data.pipeline.api.FetchDataDto;
import com.alinesno.infra.data.pipeline.entity.ReaderSourceEntity;
import com.alinesno.infra.data.pipeline.enums.DbDriverEnums;
import com.alinesno.infra.data.pipeline.scheduler.dto.SourceReader;

public class ReaderSourceMapping {

    public static SourceReader getReaderSource(ReaderSourceEntity resourceSource, FetchDataDto dto) {

        SourceReader sourceReader = new SourceReader();
        String type = resourceSource.getReaderType();

        return switch (type) {
            case "mysql", "oracle" -> genDatabaseSource(type, resourceSource, dto);
            default -> sourceReader;
        };
    }

    private static SourceReader genDatabaseSource(String type, ReaderSourceEntity resourceSource, FetchDataDto dto) {
        SourceReader reader = new SourceReader();

        reader.setJdbcUrl(resourceSource.getReaderUrl());
        reader.setDriverClass(DbDriverEnums.getDbDriver(type));
        reader.setUsername(resourceSource.getReaderUsername());
        reader.setPassword(resourceSource.getReaderPasswd());

        if(dto != null){
            reader.setQuerySql(dto.getSql());
        }

        return reader;
    }
}
