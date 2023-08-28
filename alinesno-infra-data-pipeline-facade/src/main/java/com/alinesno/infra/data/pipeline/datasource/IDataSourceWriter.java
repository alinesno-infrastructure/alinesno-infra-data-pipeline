package com.alinesno.infra.data.pipeline.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.SinkWriter;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据写入
 */
public interface IDataSourceWriter {

    public void writerData(TaskInfoDto taskInfoDto, File filterFile, TransEntity trans) throws IOException, SQLException;

}
