package com.alinesno.infra.data.pipeline.datasource;

import com.alinesno.infra.data.pipeline.entity.TransEntity;
import com.alinesno.infra.data.pipeline.scheduler.dto.TaskInfoDto;

import java.io.File;
import java.sql.SQLException;

/**
 * 数据读取
 */
public interface IDataSourceReader {

    /**
     * 读取数据
     * @param taskInfoDto
     * @param jobWorkspace
     * @param trans
     * @return
     * @throws SQLException
     */
    public File readData(TaskInfoDto taskInfoDto, String jobWorkspace, TransEntity trans) throws SQLException ;

    /**
     * 禁止读取
     */
    public void destroy() ;
}
