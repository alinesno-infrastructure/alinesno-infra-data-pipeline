package com.alinesno.infra.data.pipeline.datasource;

import com.alinesno.infra.data.pipeline.entity.TransformEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 数据写入
 */
public interface IDataSourceWriter {

    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransformEntity trans) throws IOException, SQLException;

}
