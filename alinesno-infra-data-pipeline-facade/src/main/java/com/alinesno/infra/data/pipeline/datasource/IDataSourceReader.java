package com.alinesno.infra.data.pipeline.datasource;

import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;

import java.io.File;
import java.sql.SQLException;

/**
 * 数据读取
 */
public abstract class IDataSourceReader {

    public abstract File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransEntity trans) throws SQLException ;


}
