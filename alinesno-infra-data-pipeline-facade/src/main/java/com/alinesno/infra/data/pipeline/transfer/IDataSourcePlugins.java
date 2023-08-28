package com.alinesno.infra.data.pipeline.transfer;

import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;

import java.io.File;
import java.io.IOException;

/**
 * 数据过滤处理
 */
public interface IDataSourcePlugins {

    // 插件过滤处理
    File transformData(TaskInfoDto taskInfoDto, File sourceFile) throws IOException;

}
